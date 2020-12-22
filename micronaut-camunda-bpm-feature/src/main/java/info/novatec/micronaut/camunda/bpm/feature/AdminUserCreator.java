package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.transaction.SynchronousTransactionManager;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.sql.Connection;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Groups.CAMUNDA_ADMIN;
import static org.camunda.bpm.engine.authorization.Groups.GROUP_TYPE_SYSTEM;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;

/**
 * Bean creating Camunda Admin User, Group and Authorizations if {@code camunda.bpm.admin-user.id} Property is present.
 *
 * @author Titus Meyer
 */
// Implementation based on: https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/configuration/impl/custom/CreateAdminUserConfiguration.java
@Singleton
@Requires(property = "camunda.bpm.admin-user.id")
public class AdminUserCreator implements BeanCreatedEventListener<ProcessEngine> {
    private static final Logger log = LoggerFactory.getLogger(AdminUserCreator.class);

    protected final Configuration.AdminUser adminUser;
    protected final SynchronousTransactionManager<Connection> transactionManager;

    public AdminUserCreator(Configuration configuration, SynchronousTransactionManager<Connection> transactionManager) {
        adminUser = configuration.getAdminUser();
        this.transactionManager = transactionManager;
    }

    @Override
    public ProcessEngine onCreated(BeanCreatedEvent<ProcessEngine> event) {
        ProcessEngine processEngine = event.getBean();

        IdentityService identityService = processEngine.getIdentityService();
        AuthorizationService authorizationService = processEngine.getAuthorizationService();

        return transactionManager.executeWrite(
            transactionStatus -> {
                if (!userAlreadyExists(identityService, adminUser.getId())) {
                    // create user
                    createUser(identityService, adminUser);

                    // create admin group
                    if (!groupAlreadyExists(identityService, CAMUNDA_ADMIN)) {
                        createAdminGroup(identityService, CAMUNDA_ADMIN);
                    }

                    // create admin group authorizations on all built-in resources
                    createGroupAuthorizations(authorizationService, CAMUNDA_ADMIN);

                    // add user to admin group
                    identityService.createMembership(adminUser.getId(), CAMUNDA_ADMIN);

                    log.info("Created initial Admin User: {}", adminUser.getId());
                }

                return processEngine;
            }
        );
    }

    protected static boolean userAlreadyExists(IdentityService identityService, String userId) {
        return identityService.createUserQuery().userId(userId).singleResult() != null;
    }

    protected static boolean groupAlreadyExists(IdentityService identityService, String groupId) {
        return identityService.createGroupQuery().groupId(groupId).count() > 0;
    }

    protected static User createUser(IdentityService identityService, Configuration.AdminUser adminUser) {
        User newUser = identityService.newUser(adminUser.getId());
        newUser.setPassword(adminUser.getPassword());
        newUser.setFirstName(adminUser.getFirstname());
        newUser.setLastName(adminUser.getLastname());
        adminUser.getEmail().ifPresent(newUser::setEmail);
        identityService.saveUser(newUser);
        return newUser;
    }

    protected static Group createAdminGroup(IdentityService identityService, String groupId) {
        Group camundaAdminGroup = identityService.newGroup(groupId);
        camundaAdminGroup.setName("Camunda BPM Administrators");
        camundaAdminGroup.setType(GROUP_TYPE_SYSTEM);
        identityService.saveGroup(camundaAdminGroup);
        return camundaAdminGroup;
    }

    protected static void createGroupAuthorizations(AuthorizationService authorizationService, String groupId) {
        for (Resource resource : Resources.values()) {
            if (authorizationService.createAuthorizationQuery().groupIdIn(groupId).resourceType(resource).resourceId(ANY).count() == 0) {
                AuthorizationEntity groupAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
                groupAuth.setGroupId(groupId);
                groupAuth.setResource(resource);
                groupAuth.setResourceId(ANY);
                groupAuth.addPermission(ALL);
                authorizationService.saveAuthorization(groupAuth);
            }
        }
    }
}

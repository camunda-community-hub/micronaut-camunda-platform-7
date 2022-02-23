/*
 * Copyright 2020-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.micronaut.camunda.bpm.feature.initialization;

import info.novatec.micronaut.camunda.bpm.feature.Configuration;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.transaction.SynchronousTransactionManager;
import jakarta.inject.Singleton;
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

import java.sql.Connection;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Groups.CAMUNDA_ADMIN;
import static org.camunda.bpm.engine.authorization.Groups.GROUP_TYPE_SYSTEM;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;

/**
 * Bean creating Camunda Admin User, Group and Authorizations if {@code camunda.admin-user.id} Property is present.
 *
 * @author Titus Meyer
 * @author Tobias Schäfer
 */
// Implementation based on: https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/configuration/impl/custom/CreateAdminUserConfiguration.java
@Singleton
@Requires(property = "camunda.admin-user.id")
public class AdminUserCreator implements ParallelInitializationWithProcessEngine {
    private static final Logger log = LoggerFactory.getLogger(AdminUserCreator.class);

    protected final Configuration.AdminUser adminUser;
    protected final SynchronousTransactionManager<Connection> transactionManager;

    public AdminUserCreator(Configuration configuration, SynchronousTransactionManager<Connection> transactionManager) {
        adminUser = configuration.getAdminUser();
        this.transactionManager = transactionManager;
    }

    @Override
    public void execute(ProcessEngine processEngine) {
        IdentityService identityService = processEngine.getIdentityService();
        AuthorizationService authorizationService = processEngine.getAuthorizationService();
        transactionManager.executeWrite(
            transactionStatus -> {
                if (!userAlreadyExists(identityService, adminUser.getId())) {
                    createUser(identityService);

                    if (!adminGroupAlreadyExists(identityService)) {
                        createAdminGroup(identityService);
                    }

                    createAdminGroupAuthorizations(authorizationService);

                    identityService.createMembership(adminUser.getId(), CAMUNDA_ADMIN);

                    log.info("Created initial Admin User: {}", adminUser.getId());
                }
                return null;
            }
        );
    }

    protected boolean userAlreadyExists(IdentityService identityService, String userId) {
        return identityService.createUserQuery().userId(userId).singleResult() != null;
    }

    protected boolean adminGroupAlreadyExists(IdentityService identityService) {
        return identityService.createGroupQuery().groupId(CAMUNDA_ADMIN).count() > 0;
    }

    protected void createUser(IdentityService identityService) {
        User newUser = identityService.newUser(adminUser.getId());
        newUser.setPassword(adminUser.getPassword());
        newUser.setFirstName(adminUser.getFirstname().orElse(StringUtils.capitalize(adminUser.getId())));
        newUser.setLastName(adminUser.getLastname().orElse(StringUtils.capitalize(adminUser.getId())));
        newUser.setEmail(adminUser.getEmail().orElse(adminUser.getId() + "@localhost"));

        identityService.saveUser(newUser);
    }

    protected void createAdminGroup(IdentityService identityService) {
        Group camundaAdminGroup = identityService.newGroup(CAMUNDA_ADMIN);
        camundaAdminGroup.setName("Camunda Administrators");
        camundaAdminGroup.setType(GROUP_TYPE_SYSTEM);
        identityService.saveGroup(camundaAdminGroup);
    }

    protected void createAdminGroupAuthorizations(AuthorizationService authorizationService) {
        for (Resource resource : Resources.values()) {
            if (authorizationService.createAuthorizationQuery().groupIdIn(CAMUNDA_ADMIN).resourceType(resource).resourceId(ANY).count() == 0) {
                AuthorizationEntity groupAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
                groupAuth.setGroupId(CAMUNDA_ADMIN);
                groupAuth.setResource(resource);
                groupAuth.setResourceId(ANY);
                groupAuth.addPermission(ALL);
                authorizationService.saveAuthorization(groupAuth);
            }
        }
    }
}

package info.novatec.micronaut.camunda.bpm.feature.test;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.camunda.bpm.engine.*;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class CamundaServicesFactoryTest {

    @Inject
    RuntimeService runtimeService;

    @Inject
    RepositoryService repositoryService;

    @Inject
    ManagementService managementService;

    @Inject
    AuthorizationService authorizationService;

    @Inject
    CaseService caseService;

    @Inject
    DecisionService decisionService;

    @Inject
    ExternalTaskService externalTaskService;

    @Inject
    FilterService filterService;

    @Inject
    FormService formService;

    @Inject
    TaskService taskService;

    @Inject
    HistoryService historyService;

    @Inject
    IdentityService identityService;

    @Test
    void allServiceBeansAreAvailableInApplicationContext() {
        assertNotNull(runtimeService);
        assertNotNull(repositoryService);
        assertNotNull(managementService);
        assertNotNull(authorizationService);
        assertNotNull(caseService);
        assertNotNull(decisionService);
        assertNotNull(externalTaskService);
        assertNotNull(filterService);
        assertNotNull(formService);
        assertNotNull(taskService);
        assertNotNull(historyService);
        assertNotNull(identityService);
    }
}

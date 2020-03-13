package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.test.annotation.MicronautTest;
import org.camunda.bpm.engine.*;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class MicronautProcessEngineConfigurationTest {

    @Inject
    ProcessEngine processEngine;

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
    void allBeansAreAvailableInApplicationContext() {
        assertNotNull(processEngine);
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

    @Test
    void testDeploymentName() {
        assertEquals(MicronautProcessEngineConfiguration.MICRONAUT_AUTO_DEPLOYMENT_NAME, repositoryService.createDeploymentQuery().singleResult().getName());
    }
}
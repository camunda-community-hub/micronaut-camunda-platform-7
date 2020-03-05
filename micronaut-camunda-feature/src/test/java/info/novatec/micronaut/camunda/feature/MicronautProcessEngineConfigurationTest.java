package info.novatec.micronaut.camunda.feature;

import io.micronaut.test.annotation.MicronautTest;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.Deployment;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static info.novatec.micronaut.camunda.feature.MicronautProcessEngineConfiguration.MICRONAUT_AUTO_DEPLOYMENT_NAME;
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
    void testDeploymentName() { //Test musste umgeschrieben werden, da Postgres nicht jedes mal die DB cleart wie eine H2
        Deployment lastDeployment = repositoryService.createDeploymentQuery().orderByDeploymentTime().desc().list().get(0);
        assertEquals(MICRONAUT_AUTO_DEPLOYMENT_NAME, lastDeployment.getName());
    }
}
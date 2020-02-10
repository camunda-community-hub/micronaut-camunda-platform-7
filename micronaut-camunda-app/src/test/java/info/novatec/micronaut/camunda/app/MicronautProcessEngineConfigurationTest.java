package info.novatec.micronaut.camunda.app;

import io.micronaut.test.annotation.MicronautTest;
import org.camunda.bpm.engine.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

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
        Assertions.assertNotNull(processEngine);
        Assertions.assertNotNull(runtimeService);
        Assertions.assertNotNull(repositoryService);
        Assertions.assertNotNull(managementService);
        Assertions.assertNotNull(authorizationService);
        Assertions.assertNotNull(caseService);
        Assertions.assertNotNull(decisionService);
        Assertions.assertNotNull(externalTaskService);
        Assertions.assertNotNull(filterService);
        Assertions.assertNotNull(formService);
        Assertions.assertNotNull(taskService);
        Assertions.assertNotNull(historyService);
        Assertions.assertNotNull(identityService);
    }

}
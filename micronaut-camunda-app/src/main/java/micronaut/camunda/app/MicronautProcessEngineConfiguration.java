package micronaut.camunda.app;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
public class MicronautProcessEngineConfiguration {

    @Inject
    private MicronautExpressionManager micronautExpressionManager;

    /**
     * The {@link ProcessEngine} is started with the application start so that the task scheduler is started immediately.
     *
     * @return the initialized {@link ProcessEngine} in the application context.
     */
    @Singleton
    @Context
    public ProcessEngine processEngine() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
                .setJobExecutorActivate(true);

        ((ProcessEngineConfigurationImpl)processEngineConfiguration).setExpressionManager(micronautExpressionManager);

        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // TODO: Deploy all *.bpmn and *.dmn from classpath
        processEngine.getRepositoryService().createDeployment()
                .addClasspathResource("helloworld.bpmn")
                .deploy();

        return processEngine;
    }

    /**
     * Creates a bean for the {@link RuntimeService} in the application context.
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link RuntimeService}
     */
    @Singleton
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Singleton
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Singleton
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Singleton
    public AuthorizationService authorizationService(ProcessEngine processEngine) {
        return processEngine.getAuthorizationService();
    }

    @Singleton
    public CaseService caseService(ProcessEngine processEngine) {
        return processEngine.getCaseService();
    }

    @Singleton
    public DecisionService decisionService(ProcessEngine processEngine) {
        return processEngine.getDecisionService();
    }

    @Singleton
    public ExternalTaskService externalTaskService(ProcessEngine processEngine) {
        return processEngine.getExternalTaskService();
    }

    @Singleton
    public FilterService filterService(ProcessEngine processEngine) {
        return processEngine.getFilterService();
    }

    @Singleton
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Singleton
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Singleton
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Singleton
    public IdentityService identityService (ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

}

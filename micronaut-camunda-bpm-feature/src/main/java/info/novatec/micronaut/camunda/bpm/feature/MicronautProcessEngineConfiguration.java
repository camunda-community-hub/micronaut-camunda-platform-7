package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;

@Factory
public class MicronautProcessEngineConfiguration {

    public static final String MICRONAUT_AUTO_DEPLOYMENT_NAME = "MicronautAutoDeployment";

    private static final Logger log = LoggerFactory.getLogger(MicronautProcessEngineConfiguration.class);

    private final ApplicationContext applicationContext;

    private final Configuration configuration;

    private final DatasourceConfiguration datasourceConfiguration;

    public MicronautProcessEngineConfiguration(ApplicationContext applicationContext, Configuration configuration, DatasourceConfiguration datasourceConfiguration) {
        this.applicationContext = applicationContext;
        this.configuration = configuration;
        this.datasourceConfiguration = datasourceConfiguration;
    }

    /**
     * The {@link ProcessEngine} is started with the application start so that the task scheduler is started immediately.
     *
     * @return the initialized {@link ProcessEngine} in the application context.
     * @throws IOException if a resource, i.e. a model, cannot be loaded.
     */
    @Context
    public ProcessEngine processEngine() throws IOException {
        ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneProcessEngineConfiguration() {
            @Override
            public HistoryLevel getDefaultHistoryLevel() {
                // Define default history level for history level "auto".
                return HistoryLevel.HISTORY_LEVEL_FULL;
            }
        }
                .setDatabaseSchemaUpdate(configuration.getDatabase().getSchemaUpdate())
                .setJdbcUrl(datasourceConfiguration.getUrl())
                .setJdbcUsername(datasourceConfiguration.getUsername())
                .setJdbcPassword(datasourceConfiguration.getPassword())
                .setJdbcDriver(datasourceConfiguration.getDriverClassName())
                .setHistory(configuration.getHistoryLevel())
                .setJobExecutorActivate(true)
                .setExpressionManager(new MicronautExpressionManager(new ApplicationContextElResolver(applicationContext)));

        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        log.info("Successfully created process engine which is connected to database {}", datasourceConfiguration.getUrl());

        deployProcessModels(processEngine);

        return processEngine;
    }

    /**
     * Deploys all process models found in root directory of the resources.
     * <p>
     * Note: Currently this is not recursive!
     *
     * @param processEngine the {@link ProcessEngine}
     * @throws IOException if a resource, i.e. a model, cannot be loaded.
     */
    private void deployProcessModels(ProcessEngine processEngine) throws IOException {
        log.info("Searching non-recursively for models in the resources");
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        // Order of extensions has been chosen as a best fit for inter process dependencies.
        for (String extension : Arrays.asList("dmn", "cmmn", "bpmn")) {
            for (Resource resource : resourceLoader.getResources(PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "*." + extension)) {
                log.info("Deploying model: {}", resource.getFilename());
                processEngine.getRepositoryService().createDeployment()
                        .name(MICRONAUT_AUTO_DEPLOYMENT_NAME)
                        .addInputStream(resource.getFilename(), resource.getInputStream())
                        .enableDuplicateFiltering(true)
                        .deploy();
            }
        }
    }

    /**
     * Creates a bean for the {@link RuntimeService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link RuntimeService}
     */
    @Singleton
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    /**
     * Creates a bean for the {@link RepositoryService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link RepositoryService}
     */
    @Singleton
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    /**
     * Creates a bean for the {@link ManagementService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link ManagementService}
     */
    @Singleton
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    /**
     * Creates a bean for the {@link AuthorizationService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link AuthorizationService}
     */
    @Singleton
    public AuthorizationService authorizationService(ProcessEngine processEngine) {
        return processEngine.getAuthorizationService();
    }

    /**
     * Creates a bean for the {@link CaseService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link CaseService}
     */
    @Singleton
    public CaseService caseService(ProcessEngine processEngine) {
        return processEngine.getCaseService();
    }

    /**
     * Creates a bean for the {@link DecisionService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link DecisionService}
     */
    @Singleton
    public DecisionService decisionService(ProcessEngine processEngine) {
        return processEngine.getDecisionService();
    }

    /**
     * Creates a bean for the {@link ExternalTaskService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link ExternalTaskService}
     */
    @Singleton
    public ExternalTaskService externalTaskService(ProcessEngine processEngine) {
        return processEngine.getExternalTaskService();
    }

    /**
     * Creates a bean for the {@link FilterService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link FilterService}
     */
    @Singleton
    public FilterService filterService(ProcessEngine processEngine) {
        return processEngine.getFilterService();
    }

    /**
     * Creates a bean for the {@link FormService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link FormService}
     */
    @Singleton
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    /**
     * Creates a bean for the {@link TaskService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link TaskService}
     */
    @Singleton
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    /**
     * Creates a bean for the {@link HistoryService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link HistoryService}
     */
    @Singleton
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    /**
     * Creates a bean for the {@link IdentityService} in the application context which can be injected if needed.
     *
     * @param processEngine the {@link ProcessEngine}
     * @return the {@link IdentityService}
     */
    @Singleton
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

}

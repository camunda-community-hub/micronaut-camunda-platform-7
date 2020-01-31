package micronaut.camunda.app;

import io.micronaut.context.annotation.Bean;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CamundaProcessEngineConfiguration {

    @Inject
    private MicronautExpressionManager micronautExpressionManager;

    @EventListener
    @Bean
    public ProcessEngine onStartup(ServerStartupEvent event) {
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

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

}

package micronaut.camunda.app;

import io.micronaut.context.annotation.Bean;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import javax.inject.Singleton;

@Singleton
public class CamundaProcessEngineConfiguration {

    @EventListener
    @Bean
    ProcessEngine onStartup(ServerStartupEvent event) {
        ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();

        return ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
                .setJobExecutorActivate(true)
                .buildProcessEngine();

    }

}

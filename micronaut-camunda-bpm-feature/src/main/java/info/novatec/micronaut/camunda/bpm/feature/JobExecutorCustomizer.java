package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.DefaultImplementation;

/**
 * @author Aleksandr Arshavskiy
 */
@FunctionalInterface
@DefaultImplementation(DefaultJobExecutorCustomizer.class)
public interface JobExecutorCustomizer {

    void customize(MnJobExecutor jobExecutor);

}

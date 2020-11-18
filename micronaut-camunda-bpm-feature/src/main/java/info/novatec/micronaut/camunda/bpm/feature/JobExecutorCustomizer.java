package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;

import javax.annotation.Nonnull;

/**
 * @author Aleksandr Arshavskiy
 */
@FunctionalInterface
public interface JobExecutorCustomizer {

    void customize(@Nonnull JobExecutor jobExecutor);

}

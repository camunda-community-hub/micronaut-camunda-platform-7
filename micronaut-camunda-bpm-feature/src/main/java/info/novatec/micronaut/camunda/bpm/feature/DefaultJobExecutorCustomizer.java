package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
public class DefaultJobExecutorCustomizer implements JobExecutorCustomizer {

    @Override
    public void customize(@Nonnull JobExecutor jobExecutor) {
        // no customization
    }
}

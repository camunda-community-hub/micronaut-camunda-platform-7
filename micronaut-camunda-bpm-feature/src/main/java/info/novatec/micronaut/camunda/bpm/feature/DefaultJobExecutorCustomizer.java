package info.novatec.micronaut.camunda.bpm.feature;

import javax.inject.Singleton;

@Singleton
public class DefaultJobExecutorCustomizer implements JobExecutorCustomizer {

    @Override
    public void customize(MnJobExecutor jobExecutor) {
        // no customization
    }
}

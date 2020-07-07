package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.ProcessEngineConfiguration;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
public class DefaultProcessEngineConfigurationCustomizer implements ProcessEngineConfigurationCustomizer {

    @Override
    public void customize(@Nonnull ProcessEngineConfiguration configuration) {
        // no customization
    }

}

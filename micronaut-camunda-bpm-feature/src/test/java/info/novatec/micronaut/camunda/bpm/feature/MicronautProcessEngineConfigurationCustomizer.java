package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.Replaces;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@Replaces(DefaultProcessEngineConfigurationCustomizer.class)
public class MicronautProcessEngineConfigurationCustomizer implements ProcessEngineConfigurationCustomizer {

    public static final String PROCESS_ENGINE_NAME = "CustomizedEngine";

    @Override
    public void customize(@Nonnull ProcessEngineConfiguration configuration) {
        configuration.setProcessEngineName(PROCESS_ENGINE_NAME);
    }
}

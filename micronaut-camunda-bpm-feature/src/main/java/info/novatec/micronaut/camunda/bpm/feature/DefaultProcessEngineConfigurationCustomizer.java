package info.novatec.micronaut.camunda.bpm.feature;

import javax.inject.Singleton;

@Singleton
public class DefaultProcessEngineConfigurationCustomizer implements ProcessEngineConfigurationCustomizer {

    @Override
    public void customize(MnProcessEngineConfiguration processEngineConfiguration) {
        // no customization
    }

}

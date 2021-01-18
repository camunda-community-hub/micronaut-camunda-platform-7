package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.DefaultImplementation;

/**
 * @author Lukasz Frankowski
 */
@FunctionalInterface
@DefaultImplementation(DefaultProcessEngineConfigurationCustomizer.class)
public interface ProcessEngineConfigurationCustomizer {

    void customize(MnProcessEngineConfiguration processEngineConfiguration);

}

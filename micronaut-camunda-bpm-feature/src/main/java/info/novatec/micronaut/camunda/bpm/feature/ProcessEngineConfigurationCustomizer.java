package info.novatec.micronaut.camunda.bpm.feature;

/**
 * @author Lukasz Frankowski
 */
@FunctionalInterface
public interface ProcessEngineConfigurationCustomizer {

    void customize(MnProcessEngineConfiguration processEngineConfiguration);

}

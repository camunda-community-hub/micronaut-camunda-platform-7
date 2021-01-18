package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration
import info.novatec.micronaut.camunda.bpm.feature.ProcessEngineConfigurationCustomizer
import io.micronaut.context.annotation.Replaces
import javax.inject.Singleton

@Singleton
@Replaces(ProcessEngineConfigurationCustomizer::class)
class MicronautProcessEngineConfigurationCustomizer : ProcessEngineConfigurationCustomizer {
    override fun customize(processEngineConfiguration: MnProcessEngineConfiguration) {
        processEngineConfiguration.processEngineName = PROCESS_ENGINE_NAME
    }

    companion object {
        const val PROCESS_ENGINE_NAME = "CustomizedEngine"
    }
}
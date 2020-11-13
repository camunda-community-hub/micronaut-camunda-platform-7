package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.DefaultProcessEngineConfigurationCustomizer
import info.novatec.micronaut.camunda.bpm.feature.ProcessEngineConfigurationCustomizer
import io.micronaut.context.annotation.Replaces
import org.camunda.bpm.engine.ProcessEngineConfiguration
import javax.annotation.Nonnull
import javax.inject.Singleton

@Singleton
@Replaces(DefaultProcessEngineConfigurationCustomizer::class)
class MicronautProcessEngineConfigurationCustomizer : ProcessEngineConfigurationCustomizer {
    override fun customize(@Nonnull configuration: ProcessEngineConfiguration) {
        configuration.processEngineName = PROCESS_ENGINE_NAME
    }

    companion object {
        const val PROCESS_ENGINE_NAME = "CustomizedEngine"
    }
}
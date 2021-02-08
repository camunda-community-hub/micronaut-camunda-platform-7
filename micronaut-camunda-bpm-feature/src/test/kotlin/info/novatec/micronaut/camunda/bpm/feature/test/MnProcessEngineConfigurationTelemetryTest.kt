package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.CamundaBpmVersion
import io.micronaut.context.ApplicationContext
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

/**
 * Telemetry tests for [info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration].
 *
 * @author Tobias Schäfer
 */
class MnProcessEngineConfigurationTelemetryTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `telemetry is enabled for production but not for tests`(test: Boolean) {
        ApplicationContext.builder().deduceEnvironment(test).build().start().use { applicationContext ->
            val pec = applicationContext.getBean(ProcessEngine::class.java).processEngineConfiguration as ProcessEngineConfigurationImpl
            assertEquals(!test, pec.isTelemetryReporterActivate)
            assertEquals(if (test) false else null, pec.isInitializeTelemetry)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `telemetry reporter can only be enabled if version info is available`(versionInfoAvailable: Boolean) {
        val camundaBpmVersion = mock(CamundaBpmVersion::class.java)
        `when`(camundaBpmVersion.version).thenReturn(if (versionInfoAvailable) of("7.14.0") else empty())

        ApplicationContext.builder()
            .deduceEnvironment(false)
            .properties(Collections.singletonMap("camunda.bpm.generic-properties.properties.initialize-telemetry", true) as Map<String, String>)
            .build()
            .registerSingleton(CamundaBpmVersion::class.java, camundaBpmVersion)
            .start()
            .use { applicationContext ->
                val pec = applicationContext.getBean(ProcessEngine::class.java).processEngineConfiguration as ProcessEngineConfigurationImpl
                assertEquals(versionInfoAvailable, pec.isTelemetryReporterActivate)
            }
    }
}

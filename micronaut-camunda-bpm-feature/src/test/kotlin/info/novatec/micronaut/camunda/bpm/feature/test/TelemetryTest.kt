package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import javax.inject.Inject
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

@MicronautTest
class TelemetryTest {

    @Inject
    lateinit var processEngine: ProcessEngine

    @Test
    fun `telemetry is disabled in tests`() {
        val pec = processEngine.processEngineConfiguration as ProcessEngineConfigurationImpl
        assertFalse(pec.isTelemetryReporterActivate)
        assertNull(pec.isInitializeTelemetry)
    }
}

package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.MnTelemetryRegistry
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject


/**
 * Tests for [MnTelemetryRegistry]
 *
 * @author Titus Meyer
 * @author Tobias Schäfer
 */
@MicronautTest
class MnTelemetryRegistryTest {

    @Inject
    lateinit var telemetryRegistry: MnTelemetryRegistry

    @Test
    fun `integration name is set to micronaut-camunda`() {
        assertEquals("micronaut-camunda", telemetryRegistry.camundaIntegration)
    }

    @Test
    fun `application server info is set to netty`() {
        val applicationServer = telemetryRegistry.applicationServer
        assertNotNull(applicationServer)
        assertEquals("netty", applicationServer.vendor)
        assertTrue(applicationServer.version.startsWith("netty-"))
    }
}
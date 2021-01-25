package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.MnTelemetryRegistry
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject


/**
 * Tests for [MnTelemetryRegistry]
 *
 * @author Titus Meyer
 */
@MicronautTest
class MnTelemetryRegistryTest {

    @Inject
    lateinit var telemetryRegistry: MnTelemetryRegistry

    @Test
    fun `application server info is set to netty`() {
        val applicationServer = telemetryRegistry.applicationServer
        Assertions.assertNotNull(applicationServer)
        Assertions.assertEquals("netty", applicationServer.vendor)
        Assertions.assertTrue(applicationServer.version.startsWith("netty-"))
    }
}
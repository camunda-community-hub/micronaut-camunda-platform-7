package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.RuntimeException
import java.util.*

/**
 * Deprecated property tests for [info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration].
 *
 * We no longer support the "camunda.bpm" prefix.
 *
 * @author Tobias Schäfer
 */
class MnProcessEngineConfigurationDeprecatedPropertiesTest {
    @Test
    fun `valid property starts the application`() {
        val properties = Collections.singletonMap<String, Any>("camunda.generic-properties.properties.dmn-enabled", true)
        ApplicationContext.run(properties)
    }

    @Test
    fun `deprecated camunda-bpm property prevents application start`() {
        val properties = Collections.singletonMap<String, Any>("camunda.bpm.generic-properties.properties.dmn-enabled", true)
        Assertions.assertThrows(RuntimeException::class.java) { ApplicationContext.run(properties) }
    }
}
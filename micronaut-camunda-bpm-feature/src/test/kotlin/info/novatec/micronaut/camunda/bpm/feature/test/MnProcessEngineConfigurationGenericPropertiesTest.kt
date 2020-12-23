package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.ApplicationContext
import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.RuntimeException
import java.util.*

class MnProcessEngineConfigurationGenericPropertiesTest {
    @Test
    fun correctDefaultValue() {
        ApplicationContext.run().use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals("removalTimeBased", processEngineConfiguration.historyCleanupStrategy)
            assertEquals(0, processEngineConfiguration.batchJobPriority)
            assertEquals(3, processEngineConfiguration.defaultNumberOfRetries)
            assertEquals(true, processEngineConfiguration.isCmmnEnabled)
        }
    }

    /**
     * Properties are passed in as strings when used with the [io.micronaut.context.annotation.Property] annotation.
     */
    @Test
    fun validGenericPropertyAsString() {
        val properties: Map<String, Any> = mapOf(
            "camunda.bpm.generic-properties.properties.history-cleanup-strategy" to "endTimeBased",
            "camunda.bpm.generic-properties.properties.batch-job-priority" to "30",
            "camunda.bpm.generic-properties.properties.default-number-of-retries" to "1",
            "camunda.bpm.generic-properties.properties.cmmn-enabled" to "false"
        )
        ApplicationContext.run(properties).use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals("endTimeBased", processEngineConfiguration.historyCleanupStrategy)
            assertEquals(30, processEngineConfiguration.batchJobPriority)
            assertEquals(1, processEngineConfiguration.defaultNumberOfRetries)
            assertEquals(false, processEngineConfiguration.isCmmnEnabled)
        }
    }

    /**
     * Properties are passed in as primitives when using a yml file.
     */
    @Test
    fun validGenericPropertyAsPrimitive() {
        val properties: Map<String, Any> = mapOf(
            "camunda.bpm.generic-properties.properties.batch-job-priority" to 30,
            "camunda.bpm.generic-properties.properties.default-number-of-retries" to 1,
            "camunda.bpm.generic-properties.properties.cmmn-enabled" to false
        )
        ApplicationContext.run(properties).use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals(30, processEngineConfiguration.batchJobPriority)
            assertEquals(1, processEngineConfiguration.defaultNumberOfRetries)
            assertEquals(false, processEngineConfiguration.isCmmnEnabled)
        }
    }

    @Test
    fun validGenericPropertyOfTypeStringAsBoolean() {
        val properties: Map<String, Any> = mapOf(
            "camunda.bpm.generic-properties.properties.database-schema-update" to true,
        )
        ApplicationContext.run(properties).use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals("true", processEngineConfiguration.databaseSchemaUpdate)
        }
    }

    /**
     * All other tests use the properties in kebab case, i.e. lowercase and hyphen separated, let's also verify one in camel case.
     */
    @Test
    fun camelCase() {
        val properties: Map<String, Any> = mapOf(
            "camunda.bpm.generic-properties.properties.historyCleanupStrategy" to "endTimeBased"
        )
        ApplicationContext.run(properties).use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals("endTimeBased", processEngineConfiguration.historyCleanupStrategy)
        }
    }

    @Test
    fun invalidGenericProperty() {
        val properties = Collections.singletonMap<String, Any>("camunda.bpm.generic-properties.properties.xyz", "1")
        assertThrows(RuntimeException::class.java) { ApplicationContext.run(properties) }
    }
}
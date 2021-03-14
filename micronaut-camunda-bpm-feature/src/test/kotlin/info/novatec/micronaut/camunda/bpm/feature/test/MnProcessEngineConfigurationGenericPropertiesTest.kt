/*
 * Copyright 2020-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.ApplicationContext
import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.RuntimeException
import java.util.*

/**
 * Generic property tests for [info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration].
 *
 * @author Tobias Schäfer
 */
class MnProcessEngineConfigurationGenericPropertiesTest {
    @Test
    fun correctDefaultValue() {
        ApplicationContext.run().use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals("removalTimeBased", processEngineConfiguration.historyCleanupStrategy)
            assertEquals(0, processEngineConfiguration.batchJobPriority)
            assertEquals(3, processEngineConfiguration.defaultNumberOfRetries)
            assertEquals(true, processEngineConfiguration.isDmnEnabled)
        }
    }

    /**
     * Properties are passed in as strings when used with the [io.micronaut.context.annotation.Property] annotation.
     */
    @Test
    fun validGenericPropertyAsString() {
        val properties: Map<String, Any> = mapOf(
            "camunda.generic-properties.properties.history-cleanup-strategy" to "endTimeBased",
            "camunda.generic-properties.properties.batch-job-priority" to "30",
            "camunda.generic-properties.properties.default-number-of-retries" to "1",
            "camunda.generic-properties.properties.dmn-enabled" to "false", //primitive data type: boolean
            "camunda.generic-properties.properties.initialize-telemetry" to "false" //non primitive data type: Boolean
        )
        ApplicationContext.run(properties).use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals("endTimeBased", processEngineConfiguration.historyCleanupStrategy)
            assertEquals(30, processEngineConfiguration.batchJobPriority)
            assertEquals(1, processEngineConfiguration.defaultNumberOfRetries)
            assertEquals(false, processEngineConfiguration.isDmnEnabled)
            assertEquals(false, processEngineConfiguration.isInitializeTelemetry)
        }
    }

    /**
     * Properties are passed in as primitives when using a yml file.
     */
    @Test
    fun validGenericPropertyAsPrimitive() {
        val properties: Map<String, Any> = mapOf(
            "camunda.generic-properties.properties.batch-job-priority" to 30,
            "camunda.generic-properties.properties.default-number-of-retries" to 1,
            "camunda.generic-properties.properties.dmn-enabled" to false, //primitive data type: boolean
            "camunda.generic-properties.properties.initialize-telemetry" to false //non primitive data type: Boolean
        )
        ApplicationContext.run(properties).use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals(30, processEngineConfiguration.batchJobPriority)
            assertEquals(1, processEngineConfiguration.defaultNumberOfRetries)
            assertEquals(false, processEngineConfiguration.isDmnEnabled)
            assertEquals(false, processEngineConfiguration.isInitializeTelemetry)
        }
    }

    @Test
    fun validGenericPropertyOfTypeStringAsBoolean() {
        val properties: Map<String, Any> = mapOf(
            "camunda.generic-properties.properties.database-schema-update" to true,
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
            "camunda.generic-properties.properties.historyCleanupStrategy" to "endTimeBased"
        )
        ApplicationContext.run(properties).use { applicationContext ->
            val processEngineConfiguration = applicationContext.getBean(MnProcessEngineConfiguration::class.java)
            assertEquals("endTimeBased", processEngineConfiguration.historyCleanupStrategy)
        }
    }

    @Test
    fun invalidGenericProperty() {
        val properties = Collections.singletonMap<String, Any>("camunda.generic-properties.properties.xyz", "1")
        assertThrows(RuntimeException::class.java) { ApplicationContext.run(properties) }
    }
}
/*
 * Copyright 2021-2022 original authors
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

import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration
import info.novatec.micronaut.camunda.bpm.feature.initialization.LicenseKeyConfiguration
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import org.camunda.bpm.engine.ManagementService
import org.camunda.bpm.engine.ProcessEngine
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Martin Sawilla
 * @author Tobias Schäfer
 */
class LicenseKeyConfigurationTests {

    @Nested
    inner class NoLicense {

        @Test
        fun `no license provided`() {
            ApplicationContext.run(EmbeddedServer::class.java).use { embeddedServer ->
                val processEngineConfiguration = embeddedServer.applicationContext.getBean(MnProcessEngineConfiguration::class.java)
                assertEquals("removalTimeBased", processEngineConfiguration.historyCleanupStrategy)
                assertEquals(0, processEngineConfiguration.batchJobPriority)
                assertEquals(3, processEngineConfiguration.defaultNumberOfRetries)
                assertEquals(true, processEngineConfiguration.isDmnEnabled)

                assertFalse(embeddedServer.applicationContext.containsBean(LicenseKeyConfiguration::class.java))
                assertNull(embeddedServer.applicationContext.getBean(ManagementService::class.java).licenseKey)
            }
        }
    }

    @Nested
    inner class LicenseFromResource {

        @Test
        fun `load license from resource folder`() {
            val properties: Map<String, Any> = mapOf(
                "camunda.license-file" to "",
            )
            ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->
                val managementService = embeddedServer.applicationContext.getBean(ManagementService::class.java)
                val licenseKey = managementService.licenseKey
                assertEquals("micronaut-camunda-bpm", getNameOfKey(licenseKey))
                managementService.deleteLicenseKey()
            }
        }

        @Test
        fun `existing license is not overwritten`() {
            val properties: Map<String, Any> = mapOf(
                "camunda.license-file" to "",
            )
            ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->
                val managementService = embeddedServer.applicationContext.getBean(ManagementService::class.java)
                managementService.licenseKey = "test;limited"
                val licenseKeyConfiguration = embeddedServer.applicationContext.getBean(LicenseKeyConfiguration::class.java)
                licenseKeyConfiguration.execute(embeddedServer.applicationContext.getBean(ProcessEngine::class.java))
                assertEquals("limited", getNameOfKey(managementService.licenseKey))
                managementService.deleteLicenseKey()
            }
        }

        @Test
        fun `license not loaded because url is blank`() {
            val properties: Map<String, Any> = mapOf(
                "camunda.license-file" to " ",
            )
            ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->
                assertEquals(null, embeddedServer.applicationContext.getBean(ManagementService::class.java).licenseKey)
            }
        }

        @Test
        fun `license not loaded because url points to a file that does not exist`() {
            val properties: Map<String, Any> = mapOf(
                "camunda.license-file" to "file:///does-not-exist.txt",
            )
            ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->
                assertEquals(null, embeddedServer.applicationContext.getBean(ManagementService::class.java).licenseKey)
            }
        }
    }

    @Nested
    inner class LicenseFromUrl {

        @Test
        fun `load license from an url` () {
            val properties: Map<String, Any> = mapOf(
                "camunda.license-file" to LicenseFromUrl()::class.java.classLoader
                    .getResource("license/camunda-license-url.txt")!!
                    .toString()
                    .replace("file:/", "file:///"), // URI vs. URL ..
            )
            ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->
                val managementService = embeddedServer.applicationContext.getBean(ManagementService::class.java)
                val licenseKey = managementService.licenseKey
                assertEquals("micronaut-camunda-bpm-url", getNameOfKey(licenseKey))
                managementService.deleteLicenseKey()
            }
        }
    }

    private fun getNameOfKey(key: String): String {
        return key.replace("\n", "")
            .replace("\r", "")
            .replace(" ", "")
            .split(";")[1]
    }
}

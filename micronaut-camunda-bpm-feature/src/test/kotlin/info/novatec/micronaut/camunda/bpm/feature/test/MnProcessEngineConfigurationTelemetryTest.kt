/*
 * Copyright 2021 original authors
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

import info.novatec.micronaut.camunda.bpm.feature.CamundaVersion
import io.micronaut.context.ApplicationContext
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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
            assertEquals(!test, pec.isInitializeTelemetry)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `telemetry reporter can only be enabled if version info is available`(versionInfoAvailable: Boolean) {
        val camundaVersion = CamundaVersion(if (versionInfoAvailable) "7.14.0" else null)

        ApplicationContext.builder()
            .deduceEnvironment(false)
            .properties(mapOf("camunda.generic-properties.properties.initialize-telemetry" to true))
            .build()
            .registerSingleton(CamundaVersion::class.java, camundaVersion)
            .start()
            .use { applicationContext ->
                val pec = applicationContext.getBean(ProcessEngine::class.java).processEngineConfiguration as ProcessEngineConfigurationImpl
                assertEquals(versionInfoAvailable, pec.isTelemetryReporterActivate)
            }
    }
}

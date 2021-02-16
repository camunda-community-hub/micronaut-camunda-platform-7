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
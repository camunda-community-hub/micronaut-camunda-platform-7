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

import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration
import info.novatec.micronaut.camunda.bpm.feature.eventing.EventingPublisherPlugin
import info.novatec.micronaut.camunda.bpm.feature.eventing.ExecutionEvent
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.assertj.core.api.Assertions.assertThat
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.model.bpmn.Bpmn
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

class EventingPublisherPluginTest {

    @MicronautTest
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class EventingTestPluginNotLoaded : TestPropertyProvider {

        override fun getProperties(): MutableMap<String, String> {
            return mutableMapOf()
        }

        @Inject
        lateinit var processEngineConfiguration: MnProcessEngineConfiguration

        @Inject
        lateinit var eventListener: EventListener

        @Test
        fun `check that plugin is not loaded by default`() {
            val plugins = processEngineConfiguration.processEnginePlugins
            assertThat(plugins.size).isEqualTo(0)
        }

        @Test
        fun `check no events are published`() {
            assertThat(eventListener.count).isEqualTo(0)
        }
    }

    @MicronautTest
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class EventingTestPluginLoaded : TestPropertyProvider {

        override fun getProperties(): MutableMap<String, String> {
            return mutableMapOf(
                    "camunda.eventing.execution" to "true",
                    "camunda.eventing.task" to "true",
                    "camunda.eventing.history" to "true",
            )
        }

        @Inject
        lateinit var processEngineConfiguration: MnProcessEngineConfiguration

        @Inject
        lateinit var repositoryService: RepositoryService

        @Inject
        lateinit var runtimeService: RuntimeService

        @Inject
        lateinit var eventListener: EventListener

        @Test
        fun `check that plugin is loaded`() {
            val plugins = processEngineConfiguration.processEnginePlugins
            assertThat(plugins.size).isEqualTo(1)
            assertThat(plugins[0]).isInstanceOf(EventingPublisherPlugin::class.java)
        }

        @Test
        fun `check all listeners are added`() {
            // the first listener is the DefaultFailedJobParseListener, which is automatically added by ProcessEngineConfigurationImpl
            assertThat(processEngineConfiguration.customPostBPMNParseListeners.size).isEqualTo(2)
            assertThat(processEngineConfiguration.customHistoryEventHandlers.size).isEqualTo(1)
        }

        @Test
        fun `check events are published`() {
            val processId = "testProcess"
            ProcessUtil.deploy(repositoryService, Bpmn.createProcess(processId)
                    .executable()
                    .startEvent()
                    .userTask()
                    .endEvent())
            runtimeService.startProcessInstanceByKey(processId)

            assertThat(eventListener.count).isGreaterThan(0)
        }

    }

    @Singleton
    open class EventListener : ApplicationEventListener<ExecutionEvent> {

        var count = 0

        override fun onApplicationEvent(event: ExecutionEvent?) {
            count++
        }

    }
}

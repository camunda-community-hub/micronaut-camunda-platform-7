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
package info.novatec.micronaut.camunda.bpm.externaltask.process

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ProcessTest {

    @Inject
    lateinit var runtimeService: RuntimeService

    @Test
    fun happyPath() {
        val processInstance = runtimeService.startProcessInstanceByKey("externalTaskProcessExample")
        assertThat(processInstance).isStarted
        assertThat(processInstance).isWaitingAt("externalTask").externalTask().hasTopicName("my-topic")
        complete(externalTask(), withVariables("approved", true))
        assertThat(processInstance).hasPassed(
                "createItem",
                "externalTask",
                "logResult"
        )
    }

}
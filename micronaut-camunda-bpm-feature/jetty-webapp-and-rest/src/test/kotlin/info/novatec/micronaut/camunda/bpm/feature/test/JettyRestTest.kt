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

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.eclipse.jetty.server.Server
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Duration
import java.time.Instant

/**
 * Test the REST API on Jetty.
 *
 * @author Martin Sawilla
 */
@MicronautTest
@Requires(beans = [Server::class])
class JettyRestTest {

    @Inject
    lateinit var configuration: Configuration

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun engine() {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.rest.contextPath + "/engine")
        val body = client.toBlocking().retrieve(request)

        assertEquals("""[{"name":"default"}]""", body)
    }

    @ParameterizedTest
    @ValueSource(longs = [0, 2000, 5000])
    fun `long polling`(duration: Long) {
        val request: HttpRequest<String> = HttpRequest.POST(
            configuration.rest.contextPath + "/external-task/fetchAndLock",
            """
                {
                    "maxTasks": 1,
                    "workerId": "aWorkerId",
                    "asyncResponseTimeout": $duration,
                    "topics": [
                        {
                            "topicName": "aTopicName",
                            "lockDuration": 1000
                        }
                    ]
                }
            """
        )
        val start = Instant.now()
        val body = client.toBlocking().retrieve(request)

        assertEquals("[]", body)
        assertThat(Duration.between(start, Instant.now()).toMillis()).isCloseTo(duration, within(500L))
    }
}

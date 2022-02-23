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
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.eclipse.jetty.server.Server
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

/**
 * Test the customized REST API on Jetty.
 *
 * @author Martin Sawilla
 */
@MicronautTest
@Requires(beans = [Server::class])
@Property(name ="camunda.rest.context-path", value="/custom-rest-path")
@Property(name ="camunda.rest.basic-auth-enabled", value="true")
@Property(name ="camunda.admin-user.id", value="admin")
@Property(name ="camunda.admin-user.password", value="admin")
class JettyRestCustomConfigurationTest {

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

    @Test
    fun `unauthorized request`(){
        val request: HttpRequest<String> =
            HttpRequest.GET(configuration.rest.contextPath + "/user/admin/profile")

        assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().retrieve(request)
        }
    }

    @Test
    fun `test basic authentication with user profile`() {
        val request: MutableHttpRequest<String> =
            HttpRequest.GET(configuration.rest.contextPath + "/user/admin/profile")
        request.basicAuth("admin", "admin")
        val body = client.toBlocking().retrieve(request)

        assertEquals(
            """{"id":"admin","firstName":"Admin","lastName":"Admin","email":"admin@localhost"}""", body)
    }
}

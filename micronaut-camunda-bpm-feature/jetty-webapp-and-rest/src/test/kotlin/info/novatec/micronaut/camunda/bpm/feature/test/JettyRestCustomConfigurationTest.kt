package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.eclipse.jetty.server.Server
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

/**
 * Test the customized REST API on Jetty.
 *
 * @author Martin Sawilla
 */
@MicronautTest
@Requires(beans = [Server::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JettyRestCustomConfigurationTest : TestPropertyProvider {

    override fun getProperties(): MutableMap<String, String> {
        return mutableMapOf(
            "camunda.rest.context-path" to "/custom-rest-path",
            "camunda.rest.basic-auth-enabled" to "true",
            "camunda.admin-user.id" to "admin",
            "camunda.admin-user.password" to "admin",
        )
    }

    @Inject
    lateinit var configuration: Configuration

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

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

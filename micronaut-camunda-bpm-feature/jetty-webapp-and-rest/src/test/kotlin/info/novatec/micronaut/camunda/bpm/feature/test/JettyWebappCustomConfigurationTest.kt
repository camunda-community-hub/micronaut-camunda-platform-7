package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.util.CollectionUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
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
 * Test the customized Webapps on Jetty.
 *
 * @author Martin Sawilla
 */
@MicronautTest
@Requires(beans = [Server::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JettyWebappCustomConfigurationTest : TestPropertyProvider {

    override fun getProperties(): MutableMap<String, String> {
        return mutableMapOf(
            "camunda.bpm.webapps.context-path" to "/custom-webapp-path",
            "camunda.bpm.webapps.index-redirect-enabled" to "false",
            "camunda.bpm.rest.context-path" to "/custom-rest-path",
        )
    }

    @Inject
    lateinit var configuration: Configuration

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun `redirect is disabled by application configuration`() {
        val request: HttpRequest<String> = HttpRequest.GET("/")
        assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<String, Any>(request)
        }
    }

    @Test
    fun `welcome app is available` () {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/welcome/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    fun `admin app is available` () {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/admin/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    fun `cockpit app is available` () {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/cockpit/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    fun `tasklist app is available` () {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/tasklist/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }
}

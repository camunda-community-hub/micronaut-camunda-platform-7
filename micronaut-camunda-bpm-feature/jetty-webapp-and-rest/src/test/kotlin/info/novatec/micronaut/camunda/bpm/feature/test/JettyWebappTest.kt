package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.eclipse.jetty.server.Server
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

/**
 * Test the Webapps on Jetty.
 *
 * @author Martin Sawilla
 */
@MicronautTest
@Requires(beans = [Server::class])
class JettyWebappTest {

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Inject
    lateinit var configuration: Configuration

    @Test
    fun redirect() {
        val request: HttpRequest<String> = HttpRequest.GET("/")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
        assertEquals("text/html", res.header("Content-Type"))
    }

    @Test
    fun welcome() {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/welcome/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    fun admin() {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/admin/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    fun cockpit() {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/cockpit/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    fun tasklist() {
        val request: HttpRequest<String> = HttpRequest.GET(configuration.webapps.contextPath + "/app/tasklist/default")
        val res: HttpResponse<*> = client.toBlocking().exchange<String, Any>(request)
        assertEquals(HttpStatus.OK, res.status())
    }
}

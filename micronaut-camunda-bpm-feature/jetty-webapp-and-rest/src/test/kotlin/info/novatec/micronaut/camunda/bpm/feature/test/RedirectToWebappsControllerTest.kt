package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedirectToWebappsControllerTest : TestPropertyProvider {

    override fun getProperties(): MutableMap<String, String> {
        return mutableMapOf(
            "camunda.bpm.webapps.enabled" to "false",
            "camunda.bpm.webapps.index-redirect-enabled" to "true",
        )
    }

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun `redirect is disabled because webapps are disabled`() {
        val request: HttpRequest<String> = HttpRequest.GET("/")
        assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<String, Any>(request)
        }
    }
}
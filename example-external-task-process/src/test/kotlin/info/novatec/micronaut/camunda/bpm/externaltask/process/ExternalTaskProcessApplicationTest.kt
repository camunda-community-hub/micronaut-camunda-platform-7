package info.novatec.micronaut.camunda.bpm.externaltask.process

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ExternalTaskProcessApplicationTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun testItWorks() {
        assertTrue(application.isRunning)
    }
}
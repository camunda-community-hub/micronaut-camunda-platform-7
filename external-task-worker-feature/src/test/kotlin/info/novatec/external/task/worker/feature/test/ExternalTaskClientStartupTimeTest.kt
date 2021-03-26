package info.novatec.external.task.worker.feature.test

import info.novatec.external.task.worker.feature.ExternalTaskClientStartupTime
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

/**
 * @author Martin Sawilla
 */
@MicronautTest
class ExternalTaskClientStartupTimeTest {

    @Inject
    lateinit var startupTime: Optional<ExternalTaskClientStartupTime>

    @Test
    fun test() {
        Assertions.assertEquals(true, startupTime.isPresent)
    }
}
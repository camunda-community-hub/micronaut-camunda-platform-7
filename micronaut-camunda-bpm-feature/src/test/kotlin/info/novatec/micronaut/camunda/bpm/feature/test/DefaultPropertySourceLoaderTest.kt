package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.inject.Inject

/**
 * Test that [DefaultPropertySourceLoader] handles the custom properties correctly.
 *
 * @author Tobias Schäfer
 */
class DefaultPropertySourceLoaderTest {
    @MicronautTest
    @Nested
    internal inner class DefaultPropertyValue {
        @Inject
        lateinit var applicationContext: ApplicationContext

        @Test
        fun `default value`() {
            assertEquals(100,
                    applicationContext.environment.getProperty("datasources.default.maximum-pool-size", Int::class.java).get())
        }
    }

    @MicronautTest
    @Nested
    internal inner class OverwrittenPropertyValue {
        @Inject
        lateinit var applicationContext: ApplicationContext

        @Test
        @Property(name = "datasources.default.maximum-pool-size", value = "42")
        fun `property can be overwritten`() {
            assertEquals(42,
                    applicationContext.environment.getProperty("datasources.default.maximum-pool-size", Int::class.java).get())
        }
    }
}
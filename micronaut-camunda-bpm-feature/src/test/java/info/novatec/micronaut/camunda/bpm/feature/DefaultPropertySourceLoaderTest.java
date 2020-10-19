package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test that {@link DefaultPropertySourceLoader} handles the custom properties correctly.
 *
 * @author Tobias Schäfer
 */
class DefaultPropertySourceLoaderTest {

    @MicronautTest
    @Nested
    class DefaultPropertyValue {
        @Inject
        ApplicationContext applicationContext;

        @Test
        void correctDefaultValue() {
            assertEquals(100,
                    applicationContext.getEnvironment().getProperty("datasources.default.maximum-pool-size", Integer.class).get());
        }
    }

    @MicronautTest
    @Nested
    class OverwrittenPropertyValue {
        @Inject
        ApplicationContext applicationContext;

        @Test
        @Property(name = "datasources.default.maximum-pool-size", value = "42")
        void propertyCanBeOverwritten() {
            assertEquals(42,
                    applicationContext.getEnvironment().getProperty("datasources.default.maximum-pool-size", Integer.class).get());
        }
    }

}
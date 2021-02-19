package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import info.novatec.micronaut.camunda.bpm.feature.FilterAllTaskCreator
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.ProcessEngine
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

/**
 * Tests for [FilterAllTaskCreator].
 *
 * @author Martin Sawilla
 */
class FilterAllTaskCreatorTest {

    @MicronautTest
    @Nested
    inner class NoFilter {

        @Inject
        lateinit var processEngine: ProcessEngine

        @Inject
        lateinit var filterAllTaskCreator: Optional<FilterAllTaskCreator>

        @Test
        fun `filter is not created` () {
            assertFalse(filterAllTaskCreator.isPresent)
            assertEquals(0, processEngine.filterService.createFilterQuery().list().size)
        }
    }

    @MicronautTest
    @Property(name = "camunda.filter.create", value = "Custom Filter")
    @Nested
    inner class Filter {

        @Inject
        lateinit var processEngine: ProcessEngine

        @Inject
        lateinit var filterAllTaskCreator: Optional<FilterAllTaskCreator>

        @Inject
        lateinit var configuration: Configuration

        @Test
        fun `filter is created` () {
            assertTrue(filterAllTaskCreator.isPresent)
            assertEquals("Custom Filter", configuration.filter.create.get())
            assertEquals("Custom Filter", processEngine.filterService.createFilterQuery().filterName("Custom Filter").singleResult().name)
            assertEquals(1, processEngine.filterService.createFilterQuery().list().size)
        }
    }
}

package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import info.novatec.micronaut.camunda.bpm.feature.FilterAllTaskCreator
import io.micronaut.context.annotation.Property
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.ProcessEngine
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject

/**
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
    @Property(name = "camunda.bpm.filter.create", value = "Custom Filter")
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
            triggerServerStartupEvent(filterAllTaskCreator.get())
            assertEquals("Custom Filter", configuration.filter.create.get())
            assertEquals("Custom Filter", processEngine.filterService.createFilterQuery().filterName("Custom Filter").singleResult().name)
            assertEquals(1, processEngine.filterService.createFilterQuery().list().size)
        }
    }

    /**
     * Provide method to trigger event manually because we don't have an application in the feature project to fire the event
     */
    fun triggerServerStartupEvent(filterAllTaskCreator: FilterAllTaskCreator) {
        filterAllTaskCreator.onApplicationEvent(ServerStartupEvent(mock(EmbeddedServer::class.java)))
    }
}

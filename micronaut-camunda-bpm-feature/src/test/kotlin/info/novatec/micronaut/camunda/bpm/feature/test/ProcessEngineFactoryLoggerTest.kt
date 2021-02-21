package info.novatec.micronaut.camunda.bpm.feature.test

import ch.qos.logback.classic.Logger
import info.novatec.micronaut.camunda.bpm.feature.ProcessEngineFactory
import io.micronaut.context.ApplicationContext

import org.junit.jupiter.api.Test
import ch.qos.logback.core.read.ListAppender
import org.junit.jupiter.api.Assertions.assertTrue

import ch.qos.logback.classic.spi.ILoggingEvent
import org.slf4j.LoggerFactory

/**
 * @author Tobias Schäfer
 */
class ProcessEngineFactoryLoggerTest {

    @Test
    fun `Camunda version is logged on application start`() {

        val logger = LoggerFactory.getLogger(ProcessEngineFactory::class.java) as Logger

        val listAppender = ListAppender<ILoggingEvent>()
        listAppender.start()

        logger.addAppender(listAppender)

        ApplicationContext.run()

        assertTrue(listAppender.list.stream()
            .anyMatch { e -> e.formattedMessage.contains(Regex("""Camunda version: \d+\.\d+\.\d+""")) })
    }
}
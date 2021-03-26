package info.novatec.external.task.worker.feature.test

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.client.ExternalTaskClient
import org.camunda.bpm.client.impl.ExternalTaskClientImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

/**
 * @author Martin Sawilla
 */
@MicronautTest
class ExternalTaskSubscriptionTest {

    @Inject
    lateinit var externalTaskClient: ExternalTaskClient

    @Test
    fun `check topic name`() {
        val client = externalTaskClient as ExternalTaskClientImpl
        val subscriptions = client.topicSubscriptionManager.subscriptions
        assertEquals(1, subscriptions.size)
        assertEquals("test-topic", subscriptions[0].topicName)
        assertEquals(19000, subscriptions[0].lockDuration)
    }
}
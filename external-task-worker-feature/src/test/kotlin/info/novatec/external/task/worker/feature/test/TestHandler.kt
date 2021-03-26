package info.novatec.external.task.worker.feature.test

import info.novatec.external.task.worker.feature.ExternalTaskSubscription
import org.camunda.bpm.client.task.ExternalTask
import org.camunda.bpm.client.task.ExternalTaskHandler
import org.camunda.bpm.client.task.ExternalTaskService
import javax.inject.Singleton

/**
 * @author Martin Sawilla
 */
@Singleton
@ExternalTaskSubscription(topicName = "test-topic", lockDuration = 19000)
class TestHandler: ExternalTaskHandler {

    override fun execute(externalTask: ExternalTask?, externalTaskService: ExternalTaskService?) {

    }
}
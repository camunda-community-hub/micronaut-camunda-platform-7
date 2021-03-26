package info.novatec.external.task.worker.feature.test

import info.novatec.external.task.worker.feature.ExternalClientCustomizer
import io.micronaut.context.annotation.Replaces
import org.camunda.bpm.client.ExternalTaskClientBuilder
import javax.inject.Singleton

/**
 * @author Martin Sawilla
 */
@Singleton
@Replaces(ExternalClientCustomizer::class)
class TestExternalClientCustomization: ExternalClientCustomizer {

    override fun customize(builder: ExternalTaskClientBuilder) {
        builder.usePriority(false)
    }
}
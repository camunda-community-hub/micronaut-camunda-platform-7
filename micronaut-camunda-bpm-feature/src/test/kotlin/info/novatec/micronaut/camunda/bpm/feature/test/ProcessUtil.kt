package info.novatec.micronaut.camunda.bpm.feature.test

import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.model.bpmn.Bpmn
import org.camunda.bpm.model.bpmn.builder.EndEventBuilder

/**
 * @author Tobias Schäfer
 */
class ProcessUtil {

    companion object {

        /**
         * Deploys a process model created with the fluent api.
         *
         * @param repositoryService the [RepositoryService]
         * @param endEventBuilder the [EndEventBuilder] marking the end of the process model
         */
        fun deploy(repositoryService: RepositoryService, endEventBuilder: EndEventBuilder) {
            val xml = Bpmn.convertToString(endEventBuilder.done())
            repositoryService.createDeployment()
            .addString("model.bpmn", xml)
            .deploy()
        }
    }
}
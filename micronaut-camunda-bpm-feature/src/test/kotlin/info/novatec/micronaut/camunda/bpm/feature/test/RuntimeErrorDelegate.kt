package info.novatec.micronaut.camunda.bpm.feature.test

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import javax.inject.Singleton

/**
 * [JavaDelegate] which throws a [RuntimeException].
 *
 * @author Tobias Schäfer
 */
@Singleton
class RuntimeErrorDelegate : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        throw RuntimeException("RuntimeException thrown by RuntimeErrorDelegate")
    }
}
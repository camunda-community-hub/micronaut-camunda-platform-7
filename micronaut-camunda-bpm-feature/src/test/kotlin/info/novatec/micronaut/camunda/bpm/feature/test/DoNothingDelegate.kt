package info.novatec.micronaut.camunda.bpm.feature.test

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import javax.inject.Named
import javax.inject.Singleton

/**
 * [JavaDelegate] which doesn't do anything. This implementation may be replaced in other tests.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Named("doSomethingDelegate")
class DoNothingDelegate : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        // Nothing per default
    }
}
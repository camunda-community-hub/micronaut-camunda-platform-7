package info.novatec.micronaut.camunda.bpm.feature.test;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * {@link JavaDelegate} which doesn't do anything. This implementation may be replaced in other tests.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Named("doSomethingDelegate")
public class DoNothingDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        // Nothing per default
    }
}

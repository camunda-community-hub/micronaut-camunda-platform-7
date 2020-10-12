package info.novatec.micronaut.camunda.bpm.feature.test;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Singleton;

/**
 * {@link JavaDelegate} which throws a {@link RuntimeException}.
 *
 * @author Tobias Schäfer
 */
@Singleton
public class RuntimeErrorDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        throw new RuntimeException("RuntimeException thrown by RuntimeErrorDelegate");
    }
}

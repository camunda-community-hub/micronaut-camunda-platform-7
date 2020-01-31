package micronaut.camunda.app;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

// TODO: Make this a bean and reference it from the process model.
public class LoggerDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Hello World: " + delegateExecution);
    }
}

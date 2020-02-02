package micronaut.camunda.app;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoggerDelegate implements JavaDelegate {

    @Inject
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Hello World: " + delegateExecution + " " + runtimeService);
    }
}

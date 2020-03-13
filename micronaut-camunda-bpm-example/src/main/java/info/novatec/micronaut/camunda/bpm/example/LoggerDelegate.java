package info.novatec.micronaut.camunda.bpm.example;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Singleton;

@Singleton
public class LoggerDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Hello World: " + delegateExecution);
    }
}

package info.novatec.micronaut.camunda.externaltask.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class ResultDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(ResultDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("Item {} with value {} approved {}",
                execution.getVariable("itemNumber"),
                execution.getVariable("itemValue"),
                execution.getVariable("approved"));
    }
}

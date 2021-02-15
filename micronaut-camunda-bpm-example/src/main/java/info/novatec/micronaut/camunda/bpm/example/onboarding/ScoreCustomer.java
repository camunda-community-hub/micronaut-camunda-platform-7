package info.novatec.micronaut.camunda.bpm.example.onboarding;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class ScoreCustomer implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(ScoreCustomer.class);

    @Override
    public void execute(DelegateExecution execution) {
        int scoring = ThreadLocalRandom.current().nextInt(1, 100);
        log.info("Scored {} with result {}.", execution.getBusinessKey(), scoring);
        execution.setVariable("result", scoring);
    }
}

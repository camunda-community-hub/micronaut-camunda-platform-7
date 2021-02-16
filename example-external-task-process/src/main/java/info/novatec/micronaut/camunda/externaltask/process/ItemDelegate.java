package info.novatec.micronaut.camunda.externaltask.process;

import java.util.concurrent.ThreadLocalRandom;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ItemDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(ItemDelegate.class);

    private final AtomicInteger lastCount = new AtomicInteger(0);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        int itemValue = ThreadLocalRandom.current().nextInt(0, 1001);
        delegateExecution.setVariable("itemValue", itemValue);
        int itemNumber = lastCount.incrementAndGet();
        delegateExecution.setVariable("itemNumber", itemNumber);
        log.debug("Process Instance for item {} with Value {} running.", itemNumber, itemValue);
    }
}

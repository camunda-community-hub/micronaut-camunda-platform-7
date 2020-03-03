package info.novatec.micronaut.camunda.app;

import io.micronaut.test.annotation.MicronautTest;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@MicronautTest
class ProcessTest {

    @Inject
    RuntimeService runtimeService;

    @Inject
    LoggerDelegate loggerDelegate;

    @Test
    public void verifyBeanInvocationInServiceTask() throws InterruptedException {
        runtimeService.startProcessInstanceByKey("HelloWorld");
        verify(loggerDelegate, atLeast(1)).execute(any(DelegateExecution.class));
    }
}

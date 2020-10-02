package info.novatec.micronaut.camunda.bpm.example;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@MicronautTest
class ProcessTest {

    @Inject
    RuntimeService runtimeService;

    @Inject
    LoggerDelegate loggerDelegate;

    @BeforeEach
    void init() {
        reset(loggerDelegate);
    }

    @Test
    public void verifyBeanInvocationInServiceTask() {
        runtimeService.startProcessInstanceByKey("HelloWorld");
        verify(loggerDelegate).execute(any(DelegateExecution.class));
    }
}

package info.novatec.micronaut.camunda.bpm.example;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.builder.EndEventBuilder;
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
    RepositoryService repositoryService;

    @Inject
    LoggerDelegate loggerDelegate;

    @BeforeEach
    void init() {
        reset(loggerDelegate);
    }

    @Test
    void verifyBeanInvocationInServiceTaskWithExpressionDelegate() {
        String processId = "processWithExpressionDelegate";
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaDelegateExpression("${loggerDelegate}")
                .endEvent());

        runtimeService.startProcessInstanceByKey(processId);

        verify(loggerDelegate).execute(any(DelegateExecution.class));
    }

    @Test
    void verifyBeanInvocationInServiceTaskWithJavaClassName() {
        String processId = "processWithJavaClassName";
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaClass(LoggerDelegate.class.getName())
                .endEvent());

        runtimeService.startProcessInstanceByKey(processId);

        verify(loggerDelegate).execute(any(DelegateExecution.class));
    }

    @Test
    void verifyBeanInvocationInServiceTaskWithUnmanagedJavaDelegate() {
        String processId = "unmanagedJavaDelegate";
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaClass(UnmanagedJavaDelegate.class.getName())
                .endEvent());

        runtimeService.startProcessInstanceByKey(processId);
    }

    private void deploy(EndEventBuilder endEventBuilder) {
        String xml = Bpmn.convertToString(endEventBuilder.done());

        repositoryService.createDeployment()
                .addString("model.bpmn", xml)
                .deploy();
    }

    public static class UnmanagedJavaDelegate implements JavaDelegate {

        @Override
        public void execute(DelegateExecution execution) throws Exception {

        }
    }
}

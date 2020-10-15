package info.novatec.micronaut.camunda.bpm.feature.test;

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
import javax.inject.Named;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@MicronautTest
class BeanInvocationTest {

    @Inject
    RuntimeService runtimeService;

    @Inject
    RepositoryService repositoryService;

    @Inject
    @Named("myDelegate")
    JavaDelegate myDelegate;

    @BeforeEach
    void init() {
        reset(myDelegate);
    }

    @Test
    void verifyServiceTaskWithExpressionDelegate() throws Exception {
        String processId = "processWithExpressionDelegate";
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaDelegateExpression("${myDelegate}")
                .endEvent());

        runtimeService.startProcessInstanceByKey(processId);

        verify(myDelegate).execute(any(DelegateExecution.class));
    }

    @Test
    void verifyServiceTaskWithJavaClassName() throws Exception {
        String processId = "processWithJavaClassName";
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaClass(JavaDelegate.class.getName())
                .endEvent());

        runtimeService.startProcessInstanceByKey(processId);

        verify(myDelegate).execute(any(DelegateExecution.class));
    }

    @Test
    void verifyServiceTaskWithUnmanagedJavaDelegate() {
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
        public void execute(DelegateExecution execution) {

        }
    }
}

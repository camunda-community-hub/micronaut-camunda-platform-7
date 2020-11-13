package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.camunda.bpm.model.bpmn.Bpmn
import org.camunda.bpm.model.bpmn.builder.EndEventBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named

@MicronautTest
class BeanInvocationTest {
    @Inject
    lateinit var runtimeService: RuntimeService

    @Inject
    lateinit var repositoryService: RepositoryService

    @Inject
    @Named("myDelegate")
    lateinit var myDelegate: JavaDelegate

    @BeforeEach
    fun init() {
        reset(myDelegate)
    }

    @Test
    @Throws(Exception::class)
    fun `service task with expression delegate`() {
        val processId = "processWithExpressionDelegate"
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaDelegateExpression("\${myDelegate}")
                .endEvent())
        runtimeService.startProcessInstanceByKey(processId)
        verify(myDelegate).execute(ArgumentMatchers.any(DelegateExecution::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun `service task with Java class name`() {
        val processId = "processWithJavaClassName"
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaClass(JavaDelegate::class.java.name)
                .endEvent())
        runtimeService.startProcessInstanceByKey(processId)
        verify(myDelegate).execute(ArgumentMatchers.any(DelegateExecution::class.java))
    }

    @Test
    fun `service task with unmanaged Java delegate`() {
        val processId = "unmanagedJavaDelegate"
        deploy(Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .serviceTask().camundaClass(UnmanagedJavaDelegate::class.java.name)
                .endEvent())
        runtimeService.startProcessInstanceByKey(processId)
    }

    private fun deploy(endEventBuilder: EndEventBuilder) {
        val xml = Bpmn.convertToString(endEventBuilder.done())
        repositoryService.createDeployment()
                .addString("model.bpmn", xml)
                .deploy()
    }

    class UnmanagedJavaDelegate : JavaDelegate {
        override fun execute(execution: DelegateExecution) {}
    }
}
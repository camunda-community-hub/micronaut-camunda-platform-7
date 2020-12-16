package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.test.ProcessUtil.Companion.deploy
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.ScriptEvaluationException
import org.camunda.bpm.model.bpmn.Bpmn
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Tests for [MnBeansResolverFactory]
 *
 * @author Titus Meyer
 */
@MicronautTest
class MnBeansResolverFactoryTest {
    @Inject
    lateinit var runtimeService: RuntimeService

    @Inject
    lateinit var repositoryService: RepositoryService

    @Inject
    lateinit var micronautBean: MicronautBean

    @Inject
    lateinit var myNamedBean: MicronautNamedBean

    @Test
    fun `service task with groovy script and bean`() {
        val processId = "processWithScriptTaskAndBean"
        deploy(
            repositoryService,
            Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .scriptTask().scriptFormat("groovy").scriptText(
                    """
                        micronautBean.setSum(0)
                        for (int i in 1..5) {
                          int newValue = micronautBean.getSum() + i
                          micronautBean.setSum(newValue)
                        }
                    """.trimIndent()
                )
                .endEvent()
        )
        runtimeService.startProcessInstanceByKey(processId)

        assertEquals(15, micronautBean.getSum())
    }

    @Test
    fun `service task with groovy script and named bean`() {
        val processId = "processWithScriptTaskAndNamedBean"
        deploy(
            repositoryService,
            Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .scriptTask().scriptFormat("groovy").scriptText(
                    """
                        myNamedBean.setSum(0)
                        for (int i in 1..5) {
                          int newValue = myNamedBean.getSum() + i
                          myNamedBean.setSum(newValue)
                        }
                    """.trimIndent()
                )
                .endEvent()
        )
        runtimeService.startProcessInstanceByKey(processId)

        assertEquals(15, myNamedBean.getSum())
    }

    @Test
    fun `service task with groovy script and unknown bean`() {
        val processId = "processWithScriptTaskAndUnknownBean"
        deploy(
            repositoryService,
            Bpmn.createProcess(processId)
                .executable()
                .startEvent()
                .scriptTask().scriptFormat("groovy").scriptText("unknownBean.doSomething()")
                .endEvent()
        )
        assertThrows(ScriptEvaluationException::class.java) { runtimeService.startProcessInstanceByKey(processId) }
    }

    @Singleton
    open class MicronautBean {
        private var sum: Int = 0

        fun getSum(): Int {
            return sum
        }

        fun setSum(sum: Int) {
            this.sum = sum
        }

        fun reset() {
            sum = 0
        }
    }

    @Singleton
    @Named("myNamedBean")
    class MicronautNamedBean : MicronautBean()
}
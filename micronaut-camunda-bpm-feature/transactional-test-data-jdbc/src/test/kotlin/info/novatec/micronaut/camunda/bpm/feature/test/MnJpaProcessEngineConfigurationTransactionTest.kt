package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.camunda.bpm.model.bpmn.Bpmn
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Executes the inherited tests with a JPA configuration. [PersistBookDelegate] replaces [DoNothingDelegate] and persists entities.
 *
 * @author Lukasz Frankowski
 * @author Tobias Schäfer
 */
@MicronautTest(transactional = false)
class MnJpaProcessEngineConfigurationTransactionTest : MnProcessEngineConfigurationTransactionTest() {

    @Inject
    lateinit var bookRepository: BookRepository

    @BeforeEach
    override fun deployProcessModel() {
        ProcessUtil.deploy(repositoryService, Bpmn.createProcess("SaveBookProcess")
                .executable()
                .startEvent()
                .serviceTask().camundaDelegateExpression("\${saveBookDelegate}")
                .endEvent())
    }

    @Test
    override fun testCommit() {
        super.testCommit()
        assertEquals(1, bookRepository.countByTitle(WITH_COMMIT))
    }

    @Test
    override fun testRollback() {
        super.testRollback()
        assertEquals(0, bookRepository.countByTitle(WITH_ROLLBACK))
    }

    @Test
    override fun testSurroundingTransactionWithCommit() {
        super.testSurroundingTransactionWithCommit()
        assertEquals(2, bookRepository.countByTitle(TX_WITH_COMMIT))
    }

    @Test
    override fun testNoSurroundingTransactionWithCommits() {
        super.testNoSurroundingTransactionWithCommits()
        assertEquals(2, bookRepository.countByTitle(WITH_COMMIT_COMMIT))
    }

    @Test
    override fun testNoSurroundingTransactionWithCommitAndRollback() {
        super.testNoSurroundingTransactionWithCommitAndRollback()
        assertEquals(1, bookRepository.countByTitle(WITH_COMMIT_ROLLBACK))
    }

    @Test
    override fun testSurroundingTransactionWithRollback() {
        super.testSurroundingTransactionWithRollback()
        assertEquals(0, bookRepository.countByTitle(TX_WITH_ROLLBACK))
    }

    override fun startProcess(businessKey: String): String {
        return runtimeService.startProcessInstanceByKey("SaveBookProcess", businessKey).id
    }

    @Singleton
    class SaveBookDelegate : JavaDelegate {
        @Inject
        lateinit var bookRepository: BookRepository
        override fun execute(execution: DelegateExecution) {
            bookRepository.save(Book(execution.businessKey))
        }
    }
}
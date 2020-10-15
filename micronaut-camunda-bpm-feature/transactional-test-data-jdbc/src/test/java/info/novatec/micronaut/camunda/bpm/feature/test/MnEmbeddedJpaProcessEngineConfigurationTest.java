package info.novatec.micronaut.camunda.bpm.feature.test;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Executes the inherited tests with a JPA configuration. {@link PersistBookDelegate} replaces {@link DoNothingDelegate} and persists entities.
 *
 * @author Lukasz Frankowski
 * @author Tobias Schäfer
 */
@MicronautTest(transactional = false)
public class MnEmbeddedJpaProcessEngineConfigurationTest extends MnEmbeddedProcessEngineConfigurationTest {

    @Inject
    RepositoryService repositoryService;

    @Inject
    BookRepository bookRepository;

    @BeforeEach
    void deployProcessModel() {
        String xml = Bpmn.convertToString(Bpmn.createProcess("SaveBookProcess")
                .executable()
                .startEvent()
                .serviceTask().camundaDelegateExpression("${saveBookDelegate}")
                .endEvent().done());

        repositoryService.createDeployment()
                .addString("savebook.bpmn", xml)
                .deploy();
    }

    @Override
    @Test
    void testCommit() {
        super.testCommit();
        assertEquals(1, bookRepository.countByTitle(WITH_COMMIT));
    }

    @Override
    @Test
    void testRollback() {
        super.testRollback();
        assertEquals(0, bookRepository.countByTitle(WITH_ROLLBACK));
    }

    @Override
    @Test
    void testSurroundingTransactionWithCommit() {
        super.testSurroundingTransactionWithCommit();
        assertEquals(2, bookRepository.countByTitle(TX_WITH_COMMIT));
    }

    @Override
    @Test
    void testNoSurroundingTransactionWithCommits() {
        super.testNoSurroundingTransactionWithCommits();
        assertEquals(2, bookRepository.countByTitle(WITH_COMMIT_COMMIT));
    }

    @Override
    @Test
    void testNoSurroundingTransactionWithCommitAndRollback() {
        super.testNoSurroundingTransactionWithCommitAndRollback();
        assertEquals(1, bookRepository.countByTitle(WITH_COMMIT_ROLLBACK));
    }

    @Override
    @Test
    void testSurroundingTransactionWithRollback() {
        super.testSurroundingTransactionWithRollback();
        assertEquals(0, bookRepository.countByTitle(TX_WITH_ROLLBACK));
    }

    @Override
    String startProcess(String businessKey) {
        return runtimeService.startProcessInstanceByKey("SaveBookProcess", businessKey).getId();
    }

    @Singleton
    static class SaveBookDelegate implements JavaDelegate {

        @Inject
        BookRepository bookRepository;

        @Override
        public void execute(DelegateExecution execution) {
            bookRepository.save(new Book(execution.getBusinessKey()));
        }
    }
}

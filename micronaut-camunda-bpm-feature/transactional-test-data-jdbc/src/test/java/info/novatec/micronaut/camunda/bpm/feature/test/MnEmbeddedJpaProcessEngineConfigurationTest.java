package info.novatec.micronaut.camunda.bpm.feature.test;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

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
    BookRepository bookRepository;

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

    @Test
    @Override
    void testSurroundingTransactionWithCommit() {
        super.testSurroundingTransactionWithCommit();
        assertEquals(2, bookRepository.countByTitle(TX_WITH_COMMIT));
    }

    @Override
    void testNoSurroundingTransactionWithCommits() {
        super.testNoSurroundingTransactionWithCommits();
        assertEquals(2, bookRepository.countByTitle(TX_WITH_COMMIT));
    }

    @Override
    void testNoSurroundingTransactionWithCommitAndRollback() {
        super.testNoSurroundingTransactionWithCommitAndRollback();
        assertEquals(1, bookRepository.countByTitle(TX_WITH_COMMIT));
    }

    @Test
    @Override
    void testSurroundingTransactionWithRollback() {
        super.testSurroundingTransactionWithRollback();
        assertEquals(0, bookRepository.countByTitle(TX_WITH_ROLLBACK));
    }
}

package info.novatec.micronaut.camunda.bpm.feature.test;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Executes the inherited tests with a transaction manager.
 *
 * @author Lukasz Frankowski
 * @author Tobias Schäfer
 */
@MicronautTest(transactional = false)
class MnProcessEngineConfigurationTest {

	@Inject
	RuntimeService runtimeService;

	@Inject
	HistoryService historyService;

	@Inject
	SynchronousTransactionManager<Connection> transactionManager;

	static final String WITH_COMMIT = "withCommit";
	static final String WITH_ROLLBACK= "withRollback";
	static final String TX_WITH_COMMIT = "surroundingTxWithCommit";
	static final String TX_WITH_ROLLBACK = "surroundingTxWithRollback";
	static final String WITH_COMMIT_COMMIT = "noSurroundingTxWithCommit";
	static final String WITH_COMMIT_ROLLBACK = "NoSurroundingTxWithRollback";

	@Test
	void testCommit() {
		startProcess(WITH_COMMIT);
		// process has been finished with commit and we can find it in the history
		assertEquals(1, findHistoricProcessInstances(WITH_COMMIT).size());
	}

	@Test
	void testRollback() {
		assertThrows(RuntimeException.class, () -> startProcessWithRuntimeError(WITH_ROLLBACK));
		// process has been finished but rollback happened and we cannot find it in the history
		assertEquals(0, findHistoricProcessInstances(WITH_ROLLBACK).size());
	}

	@Test
	void testSurroundingTransactionWithCommit() {
		transactionManager.executeWrite(transactionStatus -> {
			try {
				startProcess(TX_WITH_COMMIT);
				return startProcess(TX_WITH_COMMIT);
			} finally {
				assertFalse(transactionStatus.isRollbackOnly());
			}
		});
		// both processes have been finished with commit and we can find them in the history
		assertEquals(2, findHistoricProcessInstances(TX_WITH_COMMIT).size());
	}

	@Test
	void testSurroundingTransactionWithRollback() {
		assertThrows(RuntimeException.class, () -> transactionManager.executeWrite(transactionStatus -> {
			try {
				startProcess(TX_WITH_ROLLBACK);
				return startProcessWithRuntimeError(TX_WITH_ROLLBACK);
			} finally {
				assertTrue(transactionStatus.isRollbackOnly());
			}
		}));
		// first process has finished successfully but rollback happened and we cannot find anything in the history
		assertEquals(0, findHistoricProcessInstances(TX_WITH_ROLLBACK).size());
	}

	@Test
	void testNoSurroundingTransactionWithCommits() {
		startProcess(WITH_COMMIT_COMMIT);
		startProcess(WITH_COMMIT_COMMIT);
		// both processes have been finished with commit and we can find them in the history
		assertEquals(2, findHistoricProcessInstances(WITH_COMMIT_COMMIT).size());
	}

	@Test
	void testNoSurroundingTransactionWithCommitAndRollback() {
		startProcess(WITH_COMMIT_ROLLBACK);
		assertThrows(RuntimeException.class, () -> startProcessWithRuntimeError(WITH_COMMIT_ROLLBACK));
		// first process has finished successfully but rollback happened and we can find only one in the history
		assertEquals(1, findHistoricProcessInstances(WITH_COMMIT_ROLLBACK).size());
	}

	String startProcess(String businessKey) {
		return runtimeService.startProcessInstanceByKey("ProcessEmpty", businessKey).getId();
	}

	String startProcessWithRuntimeError(String businessKey) {
		return runtimeService.startProcessInstanceByKey("ProcessRuntimeError", businessKey).getId();
	}

	List<HistoricProcessInstance> findHistoricProcessInstances(String businessKey) {
		return historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.list();
	}
}

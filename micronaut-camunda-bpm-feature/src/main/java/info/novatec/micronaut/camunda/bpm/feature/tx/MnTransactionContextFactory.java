package info.novatec.micronaut.camunda.bpm.feature.tx;

import io.micronaut.transaction.SynchronousTransactionManager;
import org.camunda.bpm.engine.impl.cfg.TransactionContext;
import org.camunda.bpm.engine.impl.cfg.TransactionContextFactory;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.sql.Connection;

/**
 * Micronaut specific {@link TransactionContextFactory} to open new transaction contexts.
 *
 * @author Tobias Schäfer
 */
public class MnTransactionContextFactory implements TransactionContextFactory {

  protected final SynchronousTransactionManager<Connection> transactionManager;

  public MnTransactionContextFactory(SynchronousTransactionManager<Connection> transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  public TransactionContext openTransactionContext(CommandContext commandContext) {
    return new MnTransactionContext(commandContext, transactionManager);
  }

}

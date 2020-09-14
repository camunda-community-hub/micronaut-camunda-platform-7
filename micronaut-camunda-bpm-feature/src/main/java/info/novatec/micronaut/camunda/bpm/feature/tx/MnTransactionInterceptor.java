package info.novatec.micronaut.camunda.bpm.feature.tx;

import io.micronaut.transaction.SynchronousTransactionManager;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;

import java.sql.Connection;

import static io.micronaut.transaction.TransactionDefinition.Propagation;
import static io.micronaut.transaction.TransactionDefinition.of;

/**
 * {@link CommandInterceptor} implementation which executes the command encapsulated in a transaction with the given {@link Propagation}.
 *
 * @author Tobias Schäfer
 */
public class MnTransactionInterceptor extends CommandInterceptor {

    private final SynchronousTransactionManager<Connection> transactionManager;
    private final Propagation propagation;

    public MnTransactionInterceptor(SynchronousTransactionManager<Connection> transactionManager, Propagation propagation) {
        this.transactionManager = transactionManager;
        this.propagation = propagation;
    }

    @Override
    public <T> T execute(Command<T> command) {
        return transactionManager.execute(of(propagation), transactionStatus -> next.execute(command));
    }
}

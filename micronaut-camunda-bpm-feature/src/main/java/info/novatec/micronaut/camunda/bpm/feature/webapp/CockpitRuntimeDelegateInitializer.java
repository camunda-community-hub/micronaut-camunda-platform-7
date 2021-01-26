package info.novatec.micronaut.camunda.bpm.feature.webapp;

import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.transaction.SynchronousTransactionManager;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.camunda.bpm.cockpit.Cockpit;
import org.camunda.bpm.cockpit.CockpitRuntimeDelegate;
import org.camunda.bpm.cockpit.db.CommandExecutor;
import org.camunda.bpm.cockpit.impl.DefaultCockpitRuntimeDelegate;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.sql.Connection;

/**
 * Replacing the {@link CockpitRuntimeDelegate} on server startup makes sure that the following scenario in cockpit
 * to view process instances works: Cockpit / Process Definitions / Any Definition, e.g. HelloWorld
 * by embedding the command execution in a transaction.
 *
 * Set a breakpoint in {@link JdbcTransaction#openConnection()} to see which data source is being used:
 * Works: {@link io.micronaut.configuration.jdbc.hikari.HikariUrlDataSource}
 * Doesn't work: {@link io.micronaut.transaction.jdbc.TransactionAwareDataSource}.
 * In case of TransactionAwareDataSource a {@link io.micronaut.transaction.jdbc.exceptions.CannotGetJdbcConnectionException}
 * will be thrown in {@link io.micronaut.transaction.jdbc.DataSourceUtils#doGetConnection(DataSource, boolean)} because
 * "allowCreate" is false.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Requires(property = "camunda.bpm.webapps.enabled", value="true")
public class CockpitRuntimeDelegateInitializer implements ApplicationEventListener<ServerStartupEvent> {

    private static final Logger log = LoggerFactory.getLogger(CockpitRuntimeDelegateInitializer.class);

    protected final MnProcessEngineConfiguration processEngineConfiguration;
    protected final SynchronousTransactionManager<Connection> transactionManager;

    public CockpitRuntimeDelegateInitializer(MnProcessEngineConfiguration processEngineConfiguration, SynchronousTransactionManager<Connection> transactionManager) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.transactionManager = transactionManager;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        CockpitRuntimeDelegate cockpitRuntimeDelegate = Cockpit.getRuntimeDelegate();
        Cockpit.setCockpitRuntimeDelegate(new DefaultCockpitRuntimeDelegate() {
            @Override
            protected CommandExecutor createCommandExecutor(String processEngineName) {
                return new org.camunda.bpm.cockpit.impl.db.CommandExecutorImpl(processEngineConfiguration, getMappingFiles() ) {
                    @Override
                    public <T> T executeCommand(Command<T> command) {
                        return transactionManager.executeWrite( transactionStatus -> super.executeCommand(command));
                    }
                };
            }
        });
        log.debug("Replaced CockpitRuntimeDelegate {} with {} to enable transactions for the Cockpit", cockpitRuntimeDelegate, Cockpit.getRuntimeDelegate());
    }
}

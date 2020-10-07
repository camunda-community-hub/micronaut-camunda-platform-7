package info.novatec.micronaut.camunda.bpm.feature;

import info.novatec.micronaut.camunda.bpm.feature.tx.MnTransactionContextFactory;
import info.novatec.micronaut.camunda.bpm.feature.tx.MnTransactionInterceptor;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.transaction.SynchronousTransactionManager;
import org.camunda.bpm.engine.ArtifactFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.interceptor.CommandContextInterceptor;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;
import org.camunda.bpm.engine.impl.interceptor.LogInterceptor;
import org.camunda.bpm.engine.impl.interceptor.ProcessApplicationContextInterceptor;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.micronaut.transaction.TransactionDefinition.Propagation.REQUIRED;
import static io.micronaut.transaction.TransactionDefinition.Propagation.REQUIRES_NEW;

/**
 * Micronaut implementation of {@link org.camunda.bpm.engine.ProcessEngineConfiguration} which is aware of transaction
 * management, i.e. the surrounding transaction will be used and {@link org.camunda.bpm.engine.delegate.JavaDelegate}s
 * are executed in a transaction allowing the persistence of data with micronaut-data.
 *
 * @author Tobias Schäfer
 * @author Lukasz Frankowski
 */
@Singleton
@Requires(beans = SynchronousTransactionManager.class)
public class MnEmbeddedProcessEngineConfiguration extends MnAbstractProcessEngineConfiguration {

    protected final SynchronousTransactionManager<Connection> transactionManager;

    public MnEmbeddedProcessEngineConfiguration(Configuration configuration, ApplicationContext applicationContext, ProcessEngineConfigurationCustomizer processEngineConfigurationCustomizer, DataSource dataSource, SynchronousTransactionManager<Connection> transactionManager, ArtifactFactory artifactFactory) {
        super(configuration, applicationContext, processEngineConfigurationCustomizer, artifactFactory);
        setDataSource(dataSource);
        setTransactionsExternallyManaged(true);
        this.transactionManager = transactionManager;
    }

    @Override
    public String getConnectionString() {
        try {
            return dataSource.getConnection().getMetaData().getURL();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot determine URL of datasource", e);
        }
    }

    @Override
    public ProcessEngine buildProcessEngine() {
        return transactionManager.executeWrite(
                transactionStatus -> super.buildProcessEngine()
        );
    }

    @Override
    protected void initTransactionContextFactory() {
        if(transactionContextFactory == null) {
            transactionContextFactory = new MnTransactionContextFactory(transactionManager);
        }
    }

    @Override
    protected Collection< ? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired() {
        return getCommandInterceptors(false);
    }

    @Override
    protected Collection< ? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew() {
        return getCommandInterceptors(true);
    }

    private List<CommandInterceptor> getCommandInterceptors(boolean requiresNew) {
        return Arrays.asList(
            new LogInterceptor(),
            new ProcessApplicationContextInterceptor(this),
            new MnTransactionInterceptor(transactionManager, requiresNew ? REQUIRES_NEW : REQUIRED),
            new CommandContextInterceptor(commandContextFactory, this, requiresNew)
        );
    }
}

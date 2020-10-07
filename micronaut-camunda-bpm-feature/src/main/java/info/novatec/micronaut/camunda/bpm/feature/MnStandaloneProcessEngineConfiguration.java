package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.transaction.SynchronousTransactionManager;
import org.camunda.bpm.engine.ArtifactFactory;

import javax.inject.Singleton;

/**
 * Micronaut implementation of {@link org.camunda.bpm.engine.ProcessEngineConfiguration} which is not aware of
 * transaction management, i.e. each interaction with the process engine is executed in an isolated transaction.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Requires(missingBeans = SynchronousTransactionManager.class)
public class MnStandaloneProcessEngineConfiguration extends MnAbstractProcessEngineConfiguration {

    public MnStandaloneProcessEngineConfiguration(Configuration configuration, DatasourceConfiguration datasourceConfiguration, ApplicationContext applicationContext, ProcessEngineConfigurationCustomizer processEngineConfigurationCustomizer, ArtifactFactory artifactFactory) {
        super(configuration, applicationContext, processEngineConfigurationCustomizer, artifactFactory);
        setJdbcUrl(datasourceConfiguration.getUrl());
        setJdbcUsername(datasourceConfiguration.getUsername());
        setJdbcPassword(datasourceConfiguration.getPassword());
        setJdbcDriver(datasourceConfiguration.getDriverClassName());

        processEngineConfigurationCustomizer.customize(this);
    }

    @Override
    public String getConnectionString() {
        return getJdbcUrl();
    }
}

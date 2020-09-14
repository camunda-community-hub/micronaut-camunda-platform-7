package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.ApplicationContext;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Micronaut specific (abstract) implementation of {@link org.camunda.bpm.engine.ProcessEngineConfiguration}.
 *
 * @author Tobias Schäfer
 */
public abstract class MnAbstractProcessEngineConfiguration extends StandaloneProcessEngineConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MnAbstractProcessEngineConfiguration.class);

    public MnAbstractProcessEngineConfiguration(Configuration configuration, ApplicationContext applicationContext, ProcessEngineConfigurationCustomizer processEngineConfigurationCustomizer) {
        setDatabaseSchemaUpdate(configuration.getDatabase().getSchemaUpdate());
        setHistory(configuration.getHistoryLevel());
        setJobExecutorActivate(true);
        setExpressionManager(new MicronautExpressionManager(new ApplicationContextElResolver(applicationContext)));

        processEngineConfigurationCustomizer.customize(this);
    }

    /**
     * Connection string used to connect to the {@link javax.sql.DataSource}
     *
     * @return the connection string.
     */
    public abstract String getConnectionString();

    @Override
    public ProcessEngine buildProcessEngine() {
        ProcessEngine processEngine = super.buildProcessEngine();
        log.info("Built process engine using {} connected to {}", getClass().getSimpleName(), getConnectionString());
        return processEngine;
    }

    @Override
    public HistoryLevel getDefaultHistoryLevel() {
        // Define default history level for history level "auto".
        return HistoryLevel.HISTORY_LEVEL_FULL;
    }
}

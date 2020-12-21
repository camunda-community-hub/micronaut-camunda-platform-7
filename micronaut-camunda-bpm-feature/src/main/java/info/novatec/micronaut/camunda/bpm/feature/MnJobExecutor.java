package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.impl.jobexecutor.DefaultJobExecutor;

import javax.inject.Singleton;

/**
 * Micronaut specific implementation of the {@link org.camunda.bpm.engine.impl.jobexecutor.JobExecutor}
 *
 * @author Tobias Schäfer
 */
@Singleton
public class MnJobExecutor extends DefaultJobExecutor {

    protected final JobExecutorCustomizer jobExecutorCustomizer;

    public MnJobExecutor(JobExecutorCustomizer jobExecutorCustomizer) {
        this.jobExecutorCustomizer = jobExecutorCustomizer;
        jobExecutorCustomizer.customize(this);
    }
}

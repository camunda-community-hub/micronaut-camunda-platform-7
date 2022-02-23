/*
 * Copyright 2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.novatec.micronaut.camunda.bpm.feature.initialization;

import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration;
import io.micronaut.context.BeanProvider;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.apache.ibatis.session.Configuration;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tobias Schäfer
 */
@Singleton
@Requires(property = "camunda.two-stage-process-engine", value = "true", defaultValue = "true")
public class BootstrapProcessEngineStage2 implements ParallelInitializationWithProcessEngine {

    private static final Logger log = LoggerFactory.getLogger(BootstrapProcessEngineStage2.class);

    private final BeanProvider<MnProcessEngineConfiguration> mnProcessEngineConfigurationBeanProvider;
    private final JobExecutor jobExecutor;

    public BootstrapProcessEngineStage2(BeanProvider<MnProcessEngineConfiguration> mnProcessEngineConfigurationBeanProvider, JobExecutor jobExecutor) {
        this.mnProcessEngineConfigurationBeanProvider = mnProcessEngineConfigurationBeanProvider;
        this.jobExecutor = jobExecutor;
    }

    @Override
    public void execute(ProcessEngine processEngine) {
        MnProcessEngineConfiguration mnProcessEngineConfiguration = mnProcessEngineConfigurationBeanProvider.get();
        Configuration src = mnProcessEngineConfiguration.createConfigurationStage2();
        Configuration dest = mnProcessEngineConfiguration.getSqlSessionFactory().getConfiguration();

        final AtomicInteger statements = new AtomicInteger(0);
        src.getMappedStatements().forEach( ms -> {
            if (!dest.hasStatement(ms.getId())) {
                dest.addMappedStatement(ms);
                statements.incrementAndGet();
            }
        });
        log.debug("Copied {} mapped statements. New total is {} mapped statements.", statements.get(), dest.getMappedStatements().size());

        // The process engine is now fully initialized. We can safely start the job executor.
        startJobExecutor();
    }

    protected void startJobExecutor() {
        if (jobExecutor.isAutoActivate()) {
            jobExecutor.start();
        }
    }
}

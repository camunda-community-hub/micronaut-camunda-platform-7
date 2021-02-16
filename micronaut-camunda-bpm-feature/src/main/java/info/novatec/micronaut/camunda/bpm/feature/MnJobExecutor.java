/*
 * Copyright 2020-2021 original authors
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

    public MnJobExecutor(JobExecutorCustomizer jobExecutorCustomizer) {
        jobExecutorCustomizer.customize(this);
    }
}

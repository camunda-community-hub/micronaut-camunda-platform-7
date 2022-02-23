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

import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tobias Schäfer
 */
@Singleton
public class ParallelInitializationService {

    private static final Logger log = LoggerFactory.getLogger(ParallelInitializationService.class);

    private final ExecutorService executorService;
    private final Stream<ParallelInitializationWithProcessEngine> parallelInitializations;

    public ParallelInitializationService(@Named(TaskExecutors.SCHEDULED) ExecutorService executorService, Stream<ParallelInitializationWithProcessEngine> parallelInitializations) {
        this.executorService = executorService;
        this.parallelInitializations = parallelInitializations;
    }

    public void process(ProcessEngine processEngine) {
        List<Future<ParallelInitializationWithProcessEngine>> futures = parallelInitializations
                .map(initialization -> executorService.submit(executeParallelInitialization(initialization, processEngine)))
                .collect(Collectors.toList());

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to start", e);
            }
        });
    }

    protected Callable<ParallelInitializationWithProcessEngine> executeParallelInitialization(ParallelInitializationWithProcessEngine parallelInitializationWithProcessEngine, ProcessEngine processEngine) {
        return () -> {
            Instant start = Instant.now();
            log.debug("Start {} (Requires process engine)", parallelInitializationWithProcessEngine.getClass().getSimpleName());
            parallelInitializationWithProcessEngine.execute(processEngine);
            log.debug("End {} in {}ms", parallelInitializationWithProcessEngine.getClass().getSimpleName(), ChronoUnit.MILLIS.between(start, Instant.now()));
            return parallelInitializationWithProcessEngine;
        };
    }
}

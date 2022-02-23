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

import io.micronaut.context.BeanProvider;
import io.micronaut.core.annotation.Order;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
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
public class InitProcessEngineService {

    private static final Logger log = LoggerFactory.getLogger(InitProcessEngineService.class);

    protected final BeanProvider<ProcessEngine> processEngineBeanProvider;
    protected final Stream<ParallelInitializationWithoutProcessEngine> parallelInitializationWithoutStream;
    protected final ExecutorService executorService;

    public InitProcessEngineService(BeanProvider<ProcessEngine> processEngineBeanProvider, Stream<ParallelInitializationWithoutProcessEngine> parallelInitializationWithoutStream, @Named(TaskExecutors.SCHEDULED) ExecutorService executorService) {
        this.processEngineBeanProvider = processEngineBeanProvider;
        this.parallelInitializationWithoutStream = parallelInitializationWithoutStream;
        this.executorService = executorService;
    }

    @Order(-100)
    @EventListener
    public void onEvent(ServerStartupEvent event) {

        List<Future<ParallelInitializationWithoutProcessEngine>> futures = parallelInitializationWithoutStream
                .map(initialization -> executorService.submit(executeParallelInitialization(initialization)))
                .collect(Collectors.toList());

        log.debug("Waiting for process engine to be available");
        processEngineBeanProvider.get();

        log.debug("Waiting for all parallel initializations to complete");
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to start", e);
            }
        });
        log.debug("All parallel initializations are completed");
    }

    protected Callable<ParallelInitializationWithoutProcessEngine> executeParallelInitialization(ParallelInitializationWithoutProcessEngine parallelInitializationWithoutProcessEngine) {
        return () -> {
            Instant start = Instant.now();
            log.debug("Start {} (Does not require process engine)", parallelInitializationWithoutProcessEngine.getClass().getSimpleName());
            parallelInitializationWithoutProcessEngine.execute();
            log.debug("End without {} in {}ms", parallelInitializationWithoutProcessEngine.getClass().getSimpleName(), ChronoUnit.MILLIS.between(start, Instant.now()));
            return parallelInitializationWithoutProcessEngine;
        };
    }
}

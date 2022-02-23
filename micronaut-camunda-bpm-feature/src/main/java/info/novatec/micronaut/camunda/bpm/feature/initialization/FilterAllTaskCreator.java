/*
 * Copyright 2021 original authors
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

import info.novatec.micronaut.camunda.bpm.feature.Configuration;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create a simple "show all" filter if {@code camunda.filter.create} property is present.
 *
 * @author Martin Sawilla
 */
// Implementation based on https://github.com/camunda/camunda-bpm-spring-boot-starter/blob/master/starter/src/main/java/org/camunda/bpm/spring/boot/starter/configuration/impl/custom/CreateFilterConfiguration.java
@Singleton
@Requires(property = "camunda.filter.create")
public class FilterAllTaskCreator implements ParallelInitializationWithProcessEngine {
    private static final Logger log = LoggerFactory.getLogger(FilterAllTaskCreator.class);

    protected final String filterName;

    public FilterAllTaskCreator(Configuration configuration) {
        filterName = configuration.getFilter().getCreate()
                .orElseThrow(() -> new IllegalArgumentException("If property 'camunda.filter.create' is set it must have a value."));
    }

    @Override
    public void execute(ProcessEngine processEngine) {
        FilterService filterService = processEngine.getFilterService();
        Filter filter = filterService.createFilterQuery().filterName(filterName).singleResult();
        if (filter == null) {
            filter = filterService.newTaskFilter(filterName);
            filterService.saveFilter(filter);
            log.info("Created new task filter: {}", filterName);
        }
    }
}

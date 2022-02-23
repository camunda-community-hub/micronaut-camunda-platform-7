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

package info.novatec.micronaut.camunda.bpm.feature.eventing;

import info.novatec.micronaut.camunda.bpm.feature.Configuration;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Plugin to map Camunda Events to Micronaut {@link io.micronaut.context.event.ApplicationEvent}'s.
 *
 * @author Silvan Brenner
 */
// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/event/EventPublisherPlugin.java
@Singleton
@Requires(condition = EventingPublisherPluginCondition.class)
public class EventingPublisherPlugin implements ProcessEnginePlugin {

    private static final Logger log = LoggerFactory.getLogger(EventingPublisherPlugin.class);

    protected final BpmnParseListener publishDelegateParseListener;
    protected final HistoryEventHandler publishHistoryEventHandler;

    // Configuration must be resolved during construction - otherwise code might be blocked if a parallel thread constructs a bean during execution, e.g. the ProcessEngine
    protected final boolean eventingTask;
    protected final boolean eventingExecution;
    protected final boolean eventingHistory;

    public EventingPublisherPlugin(BpmnParseListener publishDelegateParseListener, HistoryEventHandler publishHistoryEventHandler, Configuration configuration) {
        this.publishDelegateParseListener = publishDelegateParseListener;
        this.publishHistoryEventHandler = publishHistoryEventHandler;
        eventingTask = configuration.getEventing().isTask();
        eventingExecution = configuration.getEventing().isExecution();
        eventingHistory = configuration.getEventing().isHistory();
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        if (eventingTask || eventingExecution) {
            log.info("Initialized Micronaut Event Engine Plugin.");
            if (eventingTask) {
                log.info("Task events will be published as Micronaut Events.");
            } else {
                log.info("Task eventing is disabled via property.");
            }

            if (eventingExecution) {
                log.info("Execution events will be published as Micronaut Events.");
            } else {
                log.info("Execution eventing is disabled via property.");
            }
            if (processEngineConfiguration.getCustomPostBPMNParseListeners() == null) {
                processEngineConfiguration.setCustomPostBPMNParseListeners(new ArrayList<>());
            }
            processEngineConfiguration.getCustomPostBPMNParseListeners()
                    .add(publishDelegateParseListener);
        }
        if (eventingHistory) {
            log.info("History events will be published as Micronaut events.");
            processEngineConfiguration.getCustomHistoryEventHandlers()
                    .add(publishHistoryEventHandler);
        } else {
            log.info("History eventing is disabled via property.");
        }
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
    }
}

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

package info.novatec.micronaut.camunda.bpm.example;

import info.novatec.micronaut.camunda.bpm.feature.eventing.ExecutionEvent;
import info.novatec.micronaut.camunda.bpm.feature.eventing.TaskEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SampleEventListener {

    private static final Logger log = LoggerFactory.getLogger(SampleEventListener.class);

    @EventListener
    public void onExecutionEvent(ExecutionEvent event) {
        log.info("new ExecutionEvent: EventName={}, CurrentActivityName={}", event.getEventName(),
                event.getCurrentActivityName());
    }

    @EventListener
    public void onTaskEvent(TaskEvent event) {
        log.info("new TaskEvent: EventName={}, Assignee={}", event.getEventName(), event.getAssignee());
    }

    @EventListener
    public void onTaskEvent(HistoryEvent event) {
        log.info("new HistoryEvent: EventType={}", event.getEventType());
    }

}
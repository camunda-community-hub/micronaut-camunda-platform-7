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

import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;

import java.util.List;

/**
 * Event handler publishing history events as Micronaut Events.
 *
 * @author Silvan Brenner
 */
// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/event/PublishHistoryEventHandler.java
@Singleton
public class PublishHistoryEventHandler implements HistoryEventHandler {

    protected final ApplicationEventPublisher publisher;

    public PublishHistoryEventHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handleEvent(HistoryEvent historyEvent) {
        publisher.publishEvent(historyEvent);
    }

    @Override
    public void handleEvents(final List<HistoryEvent> eventList) {
        if (eventList != null) {
            eventList.forEach(this::handleEvent);
        }
    }
}
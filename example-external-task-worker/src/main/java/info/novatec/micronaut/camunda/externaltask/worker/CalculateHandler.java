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
package info.novatec.micronaut.camunda.externaltask.worker;

import info.novatec.external.task.worker.feature.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Sawilla
 *
 * This is an example handler on how to build an ExternalTaskHandler. You can register multiple handlers for different
 * topics.
 */
@Singleton
@ExternalTaskSubscription(topicName = "my-topic")
public class CalculateHandler implements ExternalTaskHandler {

    private static final Logger log = LoggerFactory.getLogger(CalculateHandler.class);

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // Put your business logic here

        // Get a process variable
        Integer item = externalTask.getVariable("itemNumber");
        Integer value = externalTask.getVariable("itemValue");

        //Process the task
        Boolean approved = (value <= 500);
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);

        // Complete the task
        externalTaskService.complete(externalTask, variables);
        log.info("Finished item {} with value {}", item, value);
    }
}

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
package info.novatec.micronaut.camunda.externaltask.worker;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.camunda.bpm.client.ExternalTaskClient;

import javax.inject.Singleton;

@Singleton
public class ExternalTaskWorker {
    private final static Logger log = Logger.getLogger(ExternalTaskWorker.class.getName());

    @EventListener
    void onStartup(ServerStartupEvent event) {
        log.info("Starting ExternalTaskClient");

        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();


        // subscribe to an external task topic as specified in the process
        client.subscribe("my-topic")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
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
                    log.info("Finished item " + item + " with Value " + value);
                })
                .open();
    }
}

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
                .baseUrl("http://localhost:8080")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();


        // subscribe to an external task topic as specified in the process
        client.subscribe("item")
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

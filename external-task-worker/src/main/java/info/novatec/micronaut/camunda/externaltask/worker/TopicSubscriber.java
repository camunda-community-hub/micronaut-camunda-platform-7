package info.novatec.micronaut.camunda.externaltask.worker;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class TopicSubscriber implements ApplicationEventListener<ServerStartupEvent> {

    private static final Logger log = LoggerFactory.getLogger(TopicSubscriber.class);

    protected ExternalTaskHandler externalTaskHandler;
    protected Configuration configuration;
    protected ExternalTaskSubscription externalTaskSubscriptionAnnotation;

    TopicSubscriber(ExternalTaskHandler externalTaskHandler, Configuration configuration) {
        this.externalTaskHandler = externalTaskHandler;
        this.configuration = configuration;
        this.externalTaskSubscriptionAnnotation = externalTaskHandler.getClass().getAnnotation(ExternalTaskSubscription.class);
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        ExternalTaskClient
                .create()
                .baseUrl(configuration.getBaseUrl()).asyncResponseTimeout(1000).build()
                // below is the client part
                .subscribe(externalTaskSubscriptionAnnotation.topic())
                .handler(externalTaskHandler)
                .open();
    }
}

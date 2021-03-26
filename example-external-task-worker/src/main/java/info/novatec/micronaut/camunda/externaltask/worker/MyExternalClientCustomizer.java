package info.novatec.micronaut.camunda.externaltask.worker;

import info.novatec.external.task.worker.feature.ExternalClientCustomizer;
import io.micronaut.context.annotation.Replaces;
import org.camunda.bpm.client.ExternalTaskClientBuilder;

import javax.inject.Singleton;

@Singleton
@Replaces(ExternalClientCustomizer.class)
public class MyExternalClientCustomizer implements ExternalClientCustomizer {

    @Override
    public void customize(ExternalTaskClientBuilder builder) {
        builder.asyncResponseTimeout(1000);
    }
}

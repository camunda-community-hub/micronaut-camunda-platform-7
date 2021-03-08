package info.novatec.micronaut.camunda.externaltask.worker;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("external-client")
public interface Configuration {

    String getBaseUrl();
}

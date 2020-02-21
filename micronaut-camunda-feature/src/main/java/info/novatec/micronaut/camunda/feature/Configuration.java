package info.novatec.micronaut.camunda.feature;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.bind.annotation.Bindable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("camunda.bpm")
public interface Configuration {

    @NotNull
    Database getDatabase();

    @ConfigurationProperties("database")
    interface Database {

        @NotBlank
        @Bindable(defaultValue = "true")
        String getSchemaUpdate();
    }
}

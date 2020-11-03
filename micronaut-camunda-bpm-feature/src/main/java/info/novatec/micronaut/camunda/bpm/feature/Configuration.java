package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.bind.annotation.Bindable;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Tobias Schäfer
 */
@ConfigurationProperties("camunda.bpm")
public interface Configuration {

    @NotBlank
    @Bindable(defaultValue = ProcessEngineConfiguration.HISTORY_AUTO)
    String getHistoryLevel();

    @NotNull
    Database getDatabase();

    @NotNull
    Telemetry getTelemetry();

    @ConfigurationProperties("database")
    interface Database {

        @NotBlank
        @Bindable(defaultValue = ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
        String getSchemaUpdate();
    }

    @ConfigurationProperties("telemetry")
    interface Telemetry {

        @Bindable(defaultValue = "true")
        boolean isTelemetryReporterActivate();

        @Bindable(defaultValue = "false")
        boolean isInitializeTelemetry();
    }
}

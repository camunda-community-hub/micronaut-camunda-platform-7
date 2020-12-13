package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.impl.telemetry.TelemetryRegistry;

import javax.inject.Singleton;

/**
 * Micronaut specific implementation of {@link TelemetryRegistry}.
 *
 * @author Tobias Schäfer
 */
@Singleton
public class MnTelemetryRegistry extends TelemetryRegistry {

    protected static final String INTEGRATION_NAME = "micronaut-camunda-bpm";

    @Override
    public String getCamundaIntegration() {
        return INTEGRATION_NAME;
    }
}

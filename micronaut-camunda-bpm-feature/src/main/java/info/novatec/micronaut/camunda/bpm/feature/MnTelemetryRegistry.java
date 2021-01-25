package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.impl.telemetry.TelemetryRegistry;
import org.camunda.bpm.engine.impl.telemetry.dto.ApplicationServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * Micronaut specific implementation of {@link TelemetryRegistry}.
 *
 * @author Tobias Schäfer
 * @author Titus Meyer
 */
@Singleton
public class MnTelemetryRegistry extends TelemetryRegistry {
    private static final Logger log = LoggerFactory.getLogger(MnTelemetryRegistry.class);

    protected static final String INTEGRATION_NAME = "micronaut-camunda-bpm";

    public MnTelemetryRegistry(Optional<ApplicationServer> applicationServer) {
        setCamundaIntegration(INTEGRATION_NAME);
        if (applicationServer.isPresent()) {
            log.info("Server runtime version: vendor={}, version={}", applicationServer.get().getVendor(), applicationServer.get().getVersion());
            setApplicationServer(applicationServer.get());
        } else {
            log.warn("Unable to identify the application server for the telemetry data!");
        }
    }
}
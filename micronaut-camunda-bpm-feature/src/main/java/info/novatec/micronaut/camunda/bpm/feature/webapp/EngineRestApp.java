package info.novatec.micronaut.camunda.bpm.feature.webapp;

import org.camunda.bpm.webapp.impl.engine.EngineRestApplication;
import org.glassfish.jersey.server.ResourceConfig;

/***
 * @author Martin Sawilla
 */
public class EngineRestApp extends ResourceConfig {

    protected static final EngineRestApplication engineRestApplication = new EngineRestApplication();

    public EngineRestApp() {
        registerClasses(engineRestApplication.getClasses());
        // Disable WADL-Feature because we do not want to expose a XML description of our RESTful web application.
        property("jersey.config.server.wadl.disableWadl", "true");
    }
}

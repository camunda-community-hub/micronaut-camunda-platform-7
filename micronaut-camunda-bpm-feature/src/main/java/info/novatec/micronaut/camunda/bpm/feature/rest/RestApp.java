package info.novatec.micronaut.camunda.bpm.feature.rest;

import org.camunda.bpm.engine.rest.exception.ExceptionHandler;
import org.camunda.bpm.engine.rest.exception.RestExceptionHandler;
import org.camunda.bpm.engine.rest.impl.CamundaRestResources;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Register CamundaRestResources.
 *
 * See also https://docs.camunda.org/manual/latest/reference/rest/overview/embeddability/
 *
 * @author Martin Sawilla
 */

public class RestApp extends ResourceConfig {
    public RestApp() {
        // Register Camunda Rest Resources
        registerClasses(CamundaRestResources.getResourceClasses());
        registerClasses(CamundaRestResources.getConfigurationClasses());

        // If you want to configure the serialization of date fields you have to use:
        // register(JacksonConfigurator.class);
        // Then you can add the CustomJacksonDateFormatListener and configure the format.
        // See https://docs.camunda.org/manual/latest/reference/rest/overview/date-format/

        // Get proper exceptions responses
        register(RestExceptionHandler.class);
        register(ExceptionHandler.class);

        // Disable WADL-Feature because we do not want to expose a XML description of our RESTful web application.
        property("jersey.config.server.wadl.disableWadl", "true");
    }
}

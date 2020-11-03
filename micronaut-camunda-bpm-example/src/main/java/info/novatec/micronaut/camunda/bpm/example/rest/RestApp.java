package info.novatec.micronaut.camunda.bpm.example.rest;

import org.camunda.bpm.engine.rest.impl.CamundaRestResources;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Register CamundaRestResources.
 *
 * @author Martin Sawilla
 */

public class RestApp extends ResourceConfig {
    public RestApp() {
        // Register Camunda Rest Resources
        registerClasses(CamundaRestResources.getResourceClasses());
        registerClasses(CamundaRestResources.getConfigurationClasses());
    }
}
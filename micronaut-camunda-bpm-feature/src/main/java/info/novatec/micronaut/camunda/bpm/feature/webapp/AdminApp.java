package info.novatec.micronaut.camunda.bpm.feature.webapp;

import org.camunda.bpm.admin.impl.web.AdminApplication;
import org.glassfish.jersey.server.ResourceConfig;

/***
 * @author Martin Sawilla
 */
public class AdminApp extends ResourceConfig {

    protected static final AdminApplication adminApplication = new AdminApplication();

    public AdminApp() {
        registerClasses(adminApplication.getClasses());
        // Disable WADL-Feature because we do not want to expose a XML description of our RESTful web application.
        property("jersey.config.server.wadl.disableWadl", "true");
    }
}

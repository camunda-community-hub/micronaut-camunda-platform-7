package info.novatec.micronaut.camunda.bpm.feature.webapp;

import org.camunda.bpm.tasklist.impl.web.TasklistApplication;
import org.glassfish.jersey.server.ResourceConfig;

/***
 * @author Martin Sawilla
 */
public class TasklistApp extends ResourceConfig {

    static TasklistApplication tasklistApplication = new TasklistApplication();

    public TasklistApp() {
        registerClasses(tasklistApplication.getClasses());
        // Disable WADL-Feature because we do not want to expose a XML description of our RESTful web application.
        property("jersey.config.server.wadl.disableWadl", "true");
    }
}

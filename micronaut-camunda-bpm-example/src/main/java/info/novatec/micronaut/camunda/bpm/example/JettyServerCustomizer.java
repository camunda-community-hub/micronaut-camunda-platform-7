package info.novatec.micronaut.camunda.bpm.example;

import info.novatec.micronaut.camunda.bpm.example.rest.RestApp;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * Using Micronaut Servlet with Jetty to run the REST API as a servlet.
 * https://micronaut-projects.github.io/micronaut-servlet/1.0.x/guide/#jetty
 *
 * @author Martin Sawilla
 */
@Singleton
public class JettyServerCustomizer implements BeanCreatedEventListener<Server> {

    private static final Logger log = LoggerFactory.getLogger(JettyServerCustomizer.class);

    @Override
    public Server onCreated(BeanCreatedEvent<Server> event) {

        Server jettyServer = event.getBean();

        ServletContextHandler context = (ServletContextHandler) jettyServer.getHandler();

        // perform customizations...
        ServletContainer servletContainer = new ServletContainer(new RestApp());
        ServletHolder servletHolder = new ServletHolder(servletContainer);
        context.addServlet(servletHolder, "/rest/*");

        log.info("REST API initialized with Micronaut Servlet - try accessing it on http://localhost:8080/rest/engine");

        return jettyServer;
    }
}


package info.novatec.micronaut.camunda.bpm.feature;

import info.novatec.micronaut.camunda.bpm.feature.rest.RestApp;
import info.novatec.micronaut.camunda.bpm.feature.webapp.*;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import org.camunda.bpm.admin.impl.web.bootstrap.AdminContainerBootstrap;
import org.camunda.bpm.cockpit.impl.web.bootstrap.CockpitContainerBootstrap;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.camunda.bpm.engine.rest.filter.CacheControlFilter;
import org.camunda.bpm.engine.rest.filter.EmptyBodyFilter;
import org.camunda.bpm.tasklist.impl.web.bootstrap.TasklistContainerBootstrap;
import org.camunda.bpm.webapp.impl.engine.ProcessEnginesFilter;
import org.camunda.bpm.webapp.impl.security.auth.AuthenticationFilter;
import org.camunda.bpm.webapp.impl.security.filter.headersec.HttpHeaderSecurityFilter;
import org.camunda.bpm.webapp.impl.security.filter.util.HttpSessionMutexListener;
import org.camunda.bpm.welcome.impl.web.bootstrap.WelcomeContainerBootstrap;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.servlet.*;
import java.util.EnumSet;

import static javax.servlet.DispatcherType.*;

/**
 * Using Micronaut Servlet with Jetty to run the REST API as a servlet.
 *
 * see https://micronaut-projects.github.io/micronaut-servlet/latest/guide/#jetty
 *
 * @author Martin Sawilla
 */
@Singleton
//Implementation based on Spring-Boot-Starter: https://github.com/camunda/camunda-bpm-spring-boot-starter/tree/master/starter-webapp-core/src/main/java/org/camunda/bpm/spring/boot/starter/webapp
public class JettyServerCustomizer implements BeanCreatedEventListener<Server> {

    private static final Logger log = LoggerFactory.getLogger(JettyServerCustomizer.class);

    protected final Configuration configuration;

    public JettyServerCustomizer(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Server onCreated(BeanCreatedEvent<Server> event) {

        Server jettyServer = event.getBean();

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        contextHandlerCollection.addHandler(jettyServer.getHandler());

        if (configuration.getRest().isEnabled()) {
            ServletContextHandler restServletContextHandler = new ServletContextHandler();
            restServletContextHandler.setContextPath(configuration.getRest().getContextPath());
            restServletContextHandler.addServlet(new ServletHolder(new ServletContainer(new RestApp())), "/*");

            if (configuration.getRest().isBasicAuthEnabled()) {
                // see https://docs.camunda.org/manual/latest/reference/rest/overview/authentication/
                FilterHolder filterHolder = new FilterHolder(ProcessEngineAuthenticationFilter.class);
                filterHolder.setInitParameter("authentication-provider", "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
                restServletContextHandler.addFilter(filterHolder, "/*", EnumSet.of(REQUEST));
                log.debug("REST API - Basic authentication enabled");
            }

            contextHandlerCollection.addHandler(restServletContextHandler);

            log.info("REST API initialized on {}/*", configuration.getRest().getContextPath());
        }

        if (configuration.getWebapps().isEnabled()) {
            ServletContextHandler webappsContextHandler = new ServletContextHandler();
            Servlet defaultServlet = new DefaultServlet();
            ServletHolder webappsHolder = new ServletHolder("webapps", defaultServlet);
            webappsContextHandler.addServlet(webappsHolder, "/*");
            webappsContextHandler.setContextPath(configuration.getWebapps().getContextPath());

            // see https://stackoverflow.com/questions/11410388/add-more-than-one-resource-directory-to-jetty
            // and https://www.eclipse.org/jetty/documentation/jetty-9/index.html#resource-handler
            Resource webappsResource = Resource.newClassPathResource("/META-INF/resources/webjars/camunda");
            Resource pluginsResource = Resource.newClassPathResource("/META-INF/resources");
            ResourceCollection resources = new ResourceCollection(webappsResource, pluginsResource);
            webappsContextHandler.setBaseResource(resources);

            webappsContextHandler.addEventListener(new CockpitContainerBootstrap());
            webappsContextHandler.addEventListener(new AdminContainerBootstrap());
            webappsContextHandler.addEventListener(new TasklistContainerBootstrap());
            webappsContextHandler.addEventListener(new WelcomeContainerBootstrap());
            webappsContextHandler.addEventListener(new HttpSessionMutexListener());
            webappsContextHandler.addEventListener(new ServletContextInitializedListener());

            webappsContextHandler.setSessionHandler(new SessionHandler());

            contextHandlerCollection.addHandler(webappsContextHandler);

            log.info("Webapps initialized on {}", configuration.getWebapps().getContextPath());
        }

        jettyServer.setHandler(contextHandlerCollection);

        return jettyServer;
    }

    /**
      * The configuration of the Camunda Webapps is called with a ServletContextListener because in
      * JettyServerCustomizer#onCreated some underlying methods (e.g. Cockpit.getRuntimeDelegate()) would result into
      * a NullPointerException.
      */
    static class ServletContextInitializedListener implements ServletContextListener {
        private static final Logger log = LoggerFactory.getLogger(ServletContextInitializedListener.class);

        protected static EnumSet<DispatcherType> DISPATCHER_TYPES = EnumSet.of(REQUEST);

        protected static ServletContext servletContext;

        @Override
        public void contextInitialized(ServletContextEvent sce) {
            servletContext = sce.getServletContext();

            servletContext.addServlet("CockpitApp", new ServletContainer(new CockpitApp())).addMapping("/api/cockpit/*");
            servletContext.addServlet("AdminApp", new ServletContainer(new AdminApp())).addMapping("/api/admin/*");
            servletContext.addServlet("TasklistApp", new ServletContainer(new TasklistApp())).addMapping("/api/tasklist/*");
            servletContext.addServlet("EngineRestApp", new ServletContainer(new EngineRestApp())).addMapping("/api/engine/*");
            servletContext.addServlet("WelcomeApp", new ServletContainer(new WelcomeApp())).addMapping("/api/welcome/*");
            registerFilter("ProcessEnginesFilter", ProcessEnginesFilter.class, "/api/*", "/app/*");
            registerFilter("AuthenticationFilter", AuthenticationFilter.class, "/api/*", "/app/*");
            // TODO add Security Filter
            // TODO add CsrfPreventionFilter
            // TODO configure headerSecurityProperties
            registerFilter("HttpHeaderSecurityFilter", HttpHeaderSecurityFilter.class, "/api/*", "/app/*");
            registerFilter("EmptyBodyFilter", EmptyBodyFilter.class, "/api/*", "/app/*");
            registerFilter("CacheControlFilter", CacheControlFilter.class, "/api/*", "/app/*");
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {}

        protected void registerFilter(String filterName, Class<? extends Filter> filterClass, String... urlPatterns) {
            FilterRegistration filterRegistration = servletContext.getFilterRegistration(filterName);
            if (filterRegistration == null) {
                filterRegistration = servletContext.addFilter(filterName, filterClass);
                filterRegistration.addMappingForUrlPatterns(DISPATCHER_TYPES, true, urlPatterns);
                log.debug("Filter {} for URL {} registered", filterName, urlPatterns);
            }
        }
    }
}


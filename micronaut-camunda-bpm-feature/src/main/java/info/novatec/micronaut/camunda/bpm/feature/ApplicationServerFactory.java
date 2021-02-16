package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.exceptions.DisabledBeanException;
import io.micronaut.http.server.netty.NettyHttpServer;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.servlet.jetty.JettyServer;
import io.micronaut.servlet.tomcat.TomcatServer;
import io.micronaut.servlet.undertow.UndertowServer;
import io.undertow.Version;
import org.apache.catalina.util.ServerInfo;
import org.camunda.bpm.engine.impl.telemetry.dto.ApplicationServer;
import org.eclipse.jetty.util.Jetty;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

/**
 * Bean factory for {@link ApplicationServer} containing the embedded server version.
 * <p>
 * Note: We're not using javax.servlet.ServletContainerInitializer to not rely on micronaut-servlet and therefore
 * minimize dependencies.
 *
 * @author Titus Meyer
 */
@Factory
public class ApplicationServerFactory {
    protected final Optional<EmbeddedServer> embeddedServer;

    ApplicationServerFactory(Optional<EmbeddedServer> embeddedServer) {
        this.embeddedServer = embeddedServer;
    }

    @Singleton
    @Requires(classes = {NettyHttpServer.class, io.netty.util.Version.class})
    public ApplicationServer nettyServerInfo() {
        assertEmbeddedServerIsActive(NettyHttpServer.class);
        return io.netty.util.Version.identify().entrySet().stream()
                .min(Map.Entry.comparingByKey())
                .map(entry -> new ApplicationServer("netty-" + entry.getValue().artifactVersion()))
                .orElseThrow(() -> new DisabledBeanException("Version information is not available for Netty."));
    }

    @Singleton
    @Requires(classes = {JettyServer.class, Jetty.class})
    public ApplicationServer jettyServerInfo() {
        assertEmbeddedServerIsActive(JettyServer.class);
        return new ApplicationServer("jetty/" + Jetty.VERSION);
    }

    @Singleton
    @Requires(classes = {TomcatServer.class, ServerInfo.class})
    public ApplicationServer tomcatServerInfo() {
        assertEmbeddedServerIsActive(TomcatServer.class);
        return new ApplicationServer(ServerInfo.getServerInfo());
    }

    @Singleton
    @Requires(classes = {UndertowServer.class, Version.class})
    public ApplicationServer undertowServerInfo() {
        assertEmbeddedServerIsActive(UndertowServer.class);
        return new ApplicationServer(Version.getFullVersionString());
    }

    protected void assertEmbeddedServerIsActive(Class<?> clazz) {
        if (!embeddedServer.isPresent() || !embeddedServer.get().isServer() || !clazz.isAssignableFrom(embeddedServer.get().getClass())) {
            throw new DisabledBeanException(clazz.getName() + " is in classpath but not the active embedded server!");
        }
    }
}

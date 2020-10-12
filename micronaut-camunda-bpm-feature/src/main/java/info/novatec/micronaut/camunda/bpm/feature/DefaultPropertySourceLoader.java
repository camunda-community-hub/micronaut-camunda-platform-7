package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.env.ActiveEnvironment;
import io.micronaut.context.env.PropertySource;
import io.micronaut.context.env.PropertySourceLoader;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.order.Ordered;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link io.micronaut.context.env.PropertySourceLoader} which sets some default values
 * with lowest precedence.
 *
 * @author Tobias Schäfer
 */
public class DefaultPropertySourceLoader implements PropertySourceLoader, Ordered {

    /**
     * Position for the system property source loader in the chain.
     */
    private static final int POSITION = Ordered.LOWEST_PRECEDENCE;

    private static final int MAXIMUM_POOL_SIZE = 100;

    @Override
    public int getOrder() {
        return POSITION;
    }

    @Override
    public Optional<PropertySource> load(String resourceName, ResourceLoader resourceLoader) {
        // The (Hikari) datasource pool size must be larger than the number of competing threads
        // so that they don't get blocked while waiting for a connection.
        return Optional.of(
                PropertySource.of(
                        "micronaut-camunda-bpm-defaults",
                        Collections.singletonMap("datasources.default.maximum-pool-size", MAXIMUM_POOL_SIZE)));
    }

    @Override
    public Optional<PropertySource> loadEnv(String resourceName, ResourceLoader resourceLoader, ActiveEnvironment activeEnvironment) {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> read(String name, InputStream input) {
        return new HashMap<>();
    }
}

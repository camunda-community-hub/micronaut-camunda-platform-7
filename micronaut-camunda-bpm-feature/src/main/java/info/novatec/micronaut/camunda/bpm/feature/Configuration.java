package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.bind.annotation.Bindable;
import io.micronaut.core.convert.format.MapFormat;
import io.micronaut.core.naming.conventions.StringConvention;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tobias Schäfer
 */
@ConfigurationProperties("camunda.bpm")
public interface Configuration {

    @NotNull
    Webapps getWebapps();

    @NotNull
    Rest getRest();

    @NotNull
    AdminUser getAdminUser();

    @NotNull
    Filter getFilter();

    @NotNull
    GenericProperties getGenericProperties();

    @ConfigurationProperties("filter")
    interface Filter {

        @Bindable()
        Optional<String> getCreate();
    }

    @ConfigurationProperties("adminUser")
    interface AdminUser {

        @Bindable
        String getId();

        @Bindable
        String getPassword();

        @Bindable
        String getFirstname();

        @Bindable
        String getLastname();

        @Bindable
        Optional<String> getEmail();
    }

    @ConfigurationProperties("genericProperties")
    class GenericProperties {
        Map<String, Object> properties = new HashMap<>();

        public void setProperties(@MapFormat(transformation = MapFormat.MapTransformation.FLAT, keyFormat = StringConvention.CAMEL_CASE) Map<String, Object> properties) {
            this.properties = properties;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }
    }

    @ConfigurationProperties("webapps")
    interface Webapps {

        @Bindable(defaultValue = "false")
        boolean isEnabled();

        @Bindable(defaultValue = "/camunda")
        String getContextPath();
    }

    @ConfigurationProperties("rest")
    interface Rest {

        @Bindable(defaultValue = "false")
        boolean isEnabled();

        @Bindable(defaultValue = "/engine-rest")
        String getContextPath();

        @Bindable(defaultValue = "false")
        boolean isBasicAuthEnabled();
    }

}

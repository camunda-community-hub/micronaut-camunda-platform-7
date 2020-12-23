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
    AdminUser getAdminUser();

    @NotNull
    GenericProperties getGenericProperties();

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
}

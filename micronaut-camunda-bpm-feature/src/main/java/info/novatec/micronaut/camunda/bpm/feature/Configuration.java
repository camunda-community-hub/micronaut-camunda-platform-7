/*
 * Copyright 2020-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@ConfigurationProperties( "camunda")
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

        Optional<String> getCreate();
    }

    @ConfigurationProperties("adminUser")
    interface AdminUser {

        String getId();

        String getPassword();

        Optional<String> getFirstname();

        Optional<String> getLastname();

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

        // Close #219 - This method is needed so that the auto-completion of properties works
        boolean isIndexRedirectEnabled();

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

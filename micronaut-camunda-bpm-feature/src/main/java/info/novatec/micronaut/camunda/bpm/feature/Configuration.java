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
import io.micronaut.context.annotation.Context;
import io.micronaut.core.bind.annotation.Bindable;
import io.micronaut.core.convert.format.MapFormat;
import io.micronaut.core.naming.conventions.StringConvention;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Configuration of the Micronaut Camunda Integration.
 *
 * Note: When using IntelliJ IDEA Ultimate the properties' Javadoc will be shown in the auto-completion of the properties.
 *
 * @author Tobias Schäfer
 */
@Context
@ConfigurationProperties( "camunda")
public interface Configuration {

    /**
     * List of locations to scan for model files (default is the resources's root only).
     *
     * @return the list of locations.
     */
    @Bindable(defaultValue = "classpath:.")
    String[] getLocations();

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

    /**
     * Provide a URL to a license file; if no URL is present it will check your classpath for a file called "camunda-license.txt".
     *
     * @return the URL
     */
    Optional<URL> getLicenseFile();

    @ConfigurationProperties("filter")
    interface Filter {

        /**
         * Name of a "show all" filter for the task list.
         *
         * @return the name of the filter
         */
        Optional<String> getCreate();
    }

    @ConfigurationProperties("adminUser")
    interface AdminUser {

        /**
         * If present, a Camunda admin account will be created by this id (including admin group and authorizations).
         *
         * @return the ID
         */
        String getId();

        /**
         * Admin's password (mandatory if the id is present).
         *
         * @return the password
         */
        String getPassword();

        /**
         * Admin's first name (optional, defaults to the capitalized id).
         *
         * @return the first name
         */
        Optional<String> getFirstname();

        /**
         * Admin's last name (optional, defaults to the capitalized id).
         *
         * @return the last name
         */
        Optional<String> getLastname();

        /**
         * Admin's email address (optional, defaults to "id"@localhost).
         *
         * @return the email
         */
        Optional<String> getEmail();
    }

    @ConfigurationProperties("genericProperties")
    class GenericProperties {
        Map<String, Object> properties = new HashMap<>();

        /**
         * Key-value pairs of generic properties supported by the process engine configuration.
         *
         * @param properties key-value pairs of generic properties.
         */
        public void setProperties(@MapFormat(transformation = MapFormat.MapTransformation.FLAT, keyFormat = StringConvention.CAMEL_CASE) Map<String, Object> properties) {
            this.properties = properties;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }
    }

    @ConfigurationProperties("webapps")
    interface Webapps {

        /**
         * Enable the Webapps (Cockpit, Task list, Admin).
         *
         * @return is enabled?
         */
        @Bindable(defaultValue = "false")
        boolean isEnabled();

        /**
         * Context path for the Webapps.
         *
         * @return the context path
         */
        @Bindable(defaultValue = "/camunda")
        String getContextPath();

        /**
         * Registers a redirect from / to the Webapps.
         *
         * @return is index redirect enabled?
         */
        // Close #219 - This method is needed so that the auto-completion of properties works
        boolean isIndexRedirectEnabled();
    }

    @ConfigurationProperties("rest")
    interface Rest {

        /**
         * Enable the REST API.
         *
         * @return is enabled?
         */
        @Bindable(defaultValue = "false")
        boolean isEnabled();

        /**
         * Context path for the REST API.
         *
         * @return the context path
         */
        @Bindable(defaultValue = "/engine-rest")
        String getContextPath();

        /**
         * Enables basic authentication for the REST API.
         *
         * @return is enabled?
         */
        @Bindable(defaultValue = "false")
        boolean isBasicAuthEnabled();
    }

}

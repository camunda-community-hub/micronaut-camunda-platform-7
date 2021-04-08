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

        @NotNull
        HeaderSecurity getHeaderSecurity();

        @ConfigurationProperties("headerSecurity")
        interface HeaderSecurity {

            /**
             * The header can be entirely disabled if set to true. Default is false.
             *
             * @return if xss protection is disabled
             */
            @Bindable(defaultValue = "false")
            boolean isXssProtectionDisabled();

            /**
             * The allowed set of values:
             * <ul>
             *     <li>BLOCK: If the browser detects a cross-site scripting attack, the page is blocked completely</li>
             *     <li>SANITIZE: If the browser detects a cross-site scripting attack, the page is sanitized from suspicious parts (value 0)</li>
             * </ul>
             * Default is BLOCK. Note:
             * <ul>
             *     <li>Is ignored when xss-protection-disabled is set to true</li>
             *     <li>Cannot be set in conjunction with xss-protection-value</li>
             * </ul>
             *
             * @return the xss protection option
             */
            Optional<String> getXssProtectionOption();

            /**
             * A custom value for the header can be specified. Default is '1; mode=block'
             * <ul>
             *     <li>Is ignored when xss-protection-disabled is set to true</li>
             *     <li>Cannot be set in conjunction with xss-protection-option</li>
             * </ul>
             *
             * @return the xss protection value
             */
            Optional<String> getXssProtectionValue();

            /**
             * The header can be entirely disabled if set to true. Default is false.
             *
             * @return if content security police is disabled
             */
            @Bindable(defaultValue = "false")
            boolean isContentSecurityPolicyDisabled();

            /**
             * A custom value for the header can be specified. Default is 'base-uri 'self''
             *
             * Note: Property is ignored when content-security-policy-disabled is set to true
             *
             * @return the content security policy value
             */
            Optional<String> getContentSecurityPolicyValue();

            /**
             * The header can be entirely disabled if set to true. Default is false
             *
             * @return if content type options header is disabled
             */
            @Bindable(defaultValue = "false")
            boolean isContentTypeOptionsDisabled();

            /**
             * A custom value for the header can be specified.
             *
             * Note: Property is ignored when content-security-policy-disabled is set to true
             *
             * @return the custom value for the content type options header
             */
            Optional<String> getContentTypeOptionsValue();

            /**
             * Set to false to enable the header. The header is disabled by default.
             *
             * @return if hsts header is enabled.
             */
            @Bindable(defaultValue = "true")
            boolean isHstsDisabled();

            /**
             * Amount of seconds, the browser should remember to access the webapp via HTTPS.
             *
             * Note:
             * <ul>
             *     <li>Corresponds by default to one year</li>
             *     <li>Is ignored when hstsDisabled is true</li>
             *     <li>Cannot be set in conjunction with hstsValue</li>
             *     <li>Allows a maximum value of 2^31-1</li>
             * </ul>
             *
             * @return the max age
             */
            Optional<Long> getHstsMaxAge();

            /**
             * HSTS is additionally to the domain of the webapp enabled for all its subdomains.
             *
             * Note:
             * <ul>
             *     <li>Is ignored when hstsDisabled is true</li>
             *     <li>Cannot be set in conjunction with hstsValue</li>
             * </ul>
             *
             * @return if subdomains are included
             */
            @Bindable(defaultValue = "true")
            boolean isHstsIncludeSubdomainsDisabled();

            /**
             * A custom value for the header can be specified. Default is 'max-age=31536000'.
             *
             * Note:
             * <ul>
             *     <li>Is ignored when hstsDisabled is true</li>
             *     <li>Cannot be set in conjunction with hstsMaxAge or hstsIncludeSubdomainsDisabled</li>
             * </ul>
             *
             * @return hsts value for the header
             */
            Optional<String> getHstsValue();
        }

        @NotNull
        Csrf getCsrf();

        @ConfigurationProperties("csrf")
        interface Csrf {
            /**
             * Sets the application expected deployment domain.
             *
             * @return the expected deployment domain
             */
            Optional<String> getTargetOrigin();

            /**
             * Sets the HTTP response status code used for a denied request.
             *
             * @return the status code for denied requests.
             */
            Optional<Integer> getDenyStatus();

            /**
             * Sets the name of the class used to generate tokens.
             *
             * @return the class name to generate tokens.
             */
            Optional<String> getRandomClass();

            /**
             * Sets additional URLs that will not be tested for the presence of a valid token.
             *
             * @return additional urls
             */
            Optional<String[]> getEntryPoints();

            /**
             * If true, the cookie flag Secure is enabled.
             *
             * @return if the flag Secure should be set
             */
            @Bindable(defaultValue = "false")
            boolean getEnableSecureCookie();

            /**
             * If set to false, the cookie flag SameSite is disabled. The default value of the SameSite cookie is
             * LAX and it can be changed via same-site-cookie-option configuration property.
             *
             * @return if the flag SameSite should be set
             */
            @Bindable(defaultValue = "true")
            boolean getEnableSameSiteCookie();

            /**
             * Can be configured either to STRICT or LAX.
             *
             * Note:
             * <ul>
             *     <li>Is ignored when enable-same-site-cookie is set to false</li>
             *     <li>Cannot be set in conjunction with same-site-cookie-value</li>
             * </ul>
             *
             * @return the SameSite Cookie Option
             */
            Optional<String> getSameSiteCookieOption();

            /**
             * A custom value for the cookie property. Note:
             * <ul>
             *     <li>Is ignored when enable-same-site-cookie is set to false</li>
             *     <li>Cannot be set in conjunction with same-site-cookie-option</li>
             * </ul>
             *
             * @return the cookie value
             */
            Optional<String> getSameSiteCookieValue();

            /**
             * A custom value to change the cookie name. Default ist 'XSRF-Token'. Note: Please make sure to
             * additionally change the cookie name for each webapp (e.g. Cockpit) separately.
             *
             * @return the cookie name
             */
            Optional<String> getCookieName();
        }
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

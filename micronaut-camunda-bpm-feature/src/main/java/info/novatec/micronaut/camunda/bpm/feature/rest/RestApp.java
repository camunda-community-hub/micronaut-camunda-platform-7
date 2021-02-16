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
package info.novatec.micronaut.camunda.bpm.feature.rest;

import org.camunda.bpm.engine.rest.exception.ExceptionHandler;
import org.camunda.bpm.engine.rest.exception.RestExceptionHandler;
import org.camunda.bpm.engine.rest.impl.CamundaRestResources;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Register CamundaRestResources.
 *
 * See also https://docs.camunda.org/manual/latest/reference/rest/overview/embeddability/
 *
 * @author Martin Sawilla
 */

public class RestApp extends ResourceConfig {
    public RestApp() {
        // Register Camunda Rest Resources
        registerClasses(CamundaRestResources.getResourceClasses());
        registerClasses(CamundaRestResources.getConfigurationClasses());

        // If you want to configure the serialization of date fields you have to use:
        // register(JacksonConfigurator.class);
        // Then you can add the CustomJacksonDateFormatListener and configure the format.
        // See https://docs.camunda.org/manual/latest/reference/rest/overview/date-format/

        // Get proper exceptions responses
        register(RestExceptionHandler.class);
        register(ExceptionHandler.class);

        // Disable WADL-Feature because we do not want to expose a XML description of our RESTful web application.
        property("jersey.config.server.wadl.disableWadl", "true");
    }
}

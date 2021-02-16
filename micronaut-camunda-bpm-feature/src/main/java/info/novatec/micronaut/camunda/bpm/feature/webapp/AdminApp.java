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
package info.novatec.micronaut.camunda.bpm.feature.webapp;

import org.camunda.bpm.admin.impl.web.AdminApplication;
import org.glassfish.jersey.server.ResourceConfig;

/***
 * @author Martin Sawilla
 */
public class AdminApp extends ResourceConfig {

    protected static final AdminApplication adminApplication = new AdminApplication();

    public AdminApp() {
        registerClasses(adminApplication.getClasses());
        // Disable WADL-Feature because we do not want to expose a XML description of our RESTful web application.
        property("jersey.config.server.wadl.disableWadl", "true");
    }
}

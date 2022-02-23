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

import info.novatec.micronaut.camunda.bpm.feature.Configuration;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.net.URI;

/***
 * Registers a temporary redirect from / to the camunda webapps.
 *
 * @author Martin Sawilla
 */
@Requires(property = "camunda.webapps.enabled", value = "true")
@Requires(property = "camunda.webapps.index-redirect-enabled", notEquals = "false", defaultValue = "true")
@Controller
public class RedirectToWebappsController {

    // Configuration must be resolved during construction - otherwise code might be blocked if a parallel thread constructs a bean during execution, e.g. the ProcessEngine
    protected final String contextPath;

    public RedirectToWebappsController(Configuration configuration) {
        contextPath = configuration.getWebapps().getContextPath();
    }

    @Get
    public HttpResponse<?> redirect () {
        return HttpResponse.temporaryRedirect(URI.create(contextPath));
    }
}

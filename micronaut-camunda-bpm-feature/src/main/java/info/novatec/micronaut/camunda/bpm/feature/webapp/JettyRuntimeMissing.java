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
import info.novatec.micronaut.camunda.bpm.feature.initialization.ParallelInitializationWithoutProcessEngine;
import io.micronaut.context.annotation.Requires;
import io.micronaut.servlet.jetty.JettyServer;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log a warning if Webapps or REST are enabled but not supported on the current server runtime.
 *
 * Currently, the Webapps and REST are only supported on Jetty.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Requires(missing = JettyServer.class)
public class JettyRuntimeMissing implements ParallelInitializationWithoutProcessEngine {

    private static final Logger log = LoggerFactory.getLogger(JettyRuntimeMissing.class);

    // Configuration must be resolved during construction - otherwise code might be blocked if a parallel thread constructs a bean during execution, e.g. the ProcessEngine
    protected final boolean webappsEnabled;
    protected final boolean restEnabled;

    public JettyRuntimeMissing(Configuration configuration) {
        webappsEnabled = configuration.getWebapps().isEnabled();
        restEnabled = configuration.getRest().isEnabled();
    }

    @Override
    public void execute() {
        if (webappsEnabled) {
            log.warn("Webapps are enabled via 'camunda.webapps.enabled' but they are not supported on the current server runtime. Please switch to Jetty, see https://github.com/camunda-community-hub/micronaut-camunda-platform-7#camunda-rest-api-and-webapps");
        }
        if (restEnabled) {
            log.warn("REST is enabled via 'camunda.rest.enabled' but it is not supported on the current server runtime. Please switch to Jetty, see https://github.com/camunda-community-hub/micronaut-camunda-platform-7#camunda-rest-api-and-webapps");
        }
    }
}

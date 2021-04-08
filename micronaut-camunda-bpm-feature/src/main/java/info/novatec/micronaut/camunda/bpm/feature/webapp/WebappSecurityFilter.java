/*
 * Copyright 2021 original authors
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

import org.camunda.bpm.webapp.impl.security.filter.SecurityFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * This filter extends the Camunda SecurityFilter to manually set the correct context path.
 *
 * @author Martin Sawilla
 */
// Implementation based on Spring Boot Starter: https://github.com/camunda/camunda-bpm-platform/blob/a7df9b45d95ec17da9befd3f7e0888b7801038c8/spring-boot-starter/starter-webapp-core/src/main/java/org/camunda/bpm/spring/boot/starter/webapp/filter/ResourceLoadingSecurityFilter.java
public class WebappSecurityFilter extends SecurityFilter {

    protected void loadFilterRules(FilterConfig filterConfig, String applicationPath) throws ServletException {
        applicationPath = filterConfig.getServletContext().getContextPath();
        super.loadFilterRules(filterConfig, applicationPath);
    }
}

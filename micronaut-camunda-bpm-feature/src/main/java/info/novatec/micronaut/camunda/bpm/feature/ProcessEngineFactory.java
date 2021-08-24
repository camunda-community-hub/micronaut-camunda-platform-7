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

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tobias Schäfer
 */
@Factory
public class ProcessEngineFactory {

    public static final String MICRONAUT_AUTO_DEPLOYMENT_NAME = "MicronautAutoDeployment";

    private static final Logger log = LoggerFactory.getLogger(ProcessEngineFactory.class);

    protected final PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();

    /**
     * The {@link ProcessEngine} is started with the application start so that the task scheduler is started immediately.
     * @param processEngineConfiguration the {@link ProcessEngineConfiguration} to build the {@link ProcessEngine}.
     * @param camundaVersion the @{@link CamundaVersion} to log on application start.
     * @param configuration the @{@link Configuration}
     * @return the initialized {@link ProcessEngine} in the application context.
     * @throws IOException if a resource, i.e. a model, cannot be loaded.
     */
    @Context
    @Bean(preDestroy = "close")
    public ProcessEngine processEngine(ProcessEngineConfiguration processEngineConfiguration, CamundaVersion camundaVersion, Configuration configuration) throws IOException {

        if (camundaVersion.getVersion().isPresent()) {
            log.info("Camunda version: {}", camundaVersion.getVersion().get());
        } else {
            log.warn("The Camunda version cannot be determined. If you created a Fat/Uber/Shadow JAR then please consider using the Micronaut Application Plugin's 'dockerBuild' task to create a Docker image.");
        }

        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        deployProcessModels(processEngine, configuration);

        return processEngine;
    }

    /**
     * Deploys all process models found in configured directories.
     *
     * @param processEngine the {@link ProcessEngine}
     * @param configuration the @{@link Configuration}
     * @throws IOException if a resource, i.e. a model, cannot be loaded.
     */
    protected void deployProcessModels(ProcessEngine processEngine, Configuration configuration) throws IOException {
        List<String> locations = Arrays.asList(configuration.getLocations());
        log.info("Searching for models in the resources at configured locations: {}", locations);

        DeploymentBuilder builder = processEngine.getRepositoryService().createDeployment()
                .name(MICRONAUT_AUTO_DEPLOYMENT_NAME)
                .enableDuplicateFiltering(true);

        boolean deploy = false;
        for (String extension : Arrays.asList("dmn", "bpmn")) {
            for (String location : locations) {
                String pattern = replacePrefix(location) + "/*." + extension;
                Resource[] models = resourceLoader.getResources(pattern);
                for (Resource model : models) {
                    builder.addInputStream(model.getFilename(), model.getInputStream());
                    log.info("Deploying model: {}{}", normalizedPath(location), model.getFilename());
                    deploy = true;
                }
            }
        }

        if (deploy) {
            builder.deploy();
        }
    }

    protected String replacePrefix(String location) {
        return location.replace("classpath:", ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX);
    }

    protected String normalizedPath(String location) {
        if (location.endsWith(".")) {
            location = location.substring(0, location.length()-1);
        }
        if (!location.endsWith("/") && !location.endsWith("classpath:")) {
            location += "/";
        }
        return location;
    }
}

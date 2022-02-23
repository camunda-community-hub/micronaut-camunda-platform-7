/*
 * Copyright 2022 original authors
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

package info.novatec.micronaut.camunda.bpm.feature.initialization;

import info.novatec.micronaut.camunda.bpm.feature.Configuration;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
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
@Singleton
public class DeployModelsExecutor implements ParallelInitializationWithProcessEngine {

    private static final Logger log = LoggerFactory.getLogger(DeployModelsExecutor.class);

    public static final String MICRONAUT_AUTO_DEPLOYMENT_NAME = "MicronautAutoDeployment";

    protected final PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();

    // Configuration must be resolved during construction - otherwise code might be blocked if a parallel thread constructs a bean during execution, e.g. the ProcessEngine
    protected final List<String> locations;

    public DeployModelsExecutor(Configuration configuration) {
        locations = Arrays.asList(configuration.getLocations());
    }

    @Override
    public void execute(ProcessEngine processEngine) throws IOException {
        deployProcessModels(processEngine.getRepositoryService());
    }

    /**
     * Deploys all process models found in configured directories.
     *
     * @param repositoryService the @{@link RepositoryService}
     *
     * @throws IOException if a resource, i.e. a model, cannot be loaded.
     */
    protected void deployProcessModels(RepositoryService repositoryService) throws IOException {
        log.info("Searching for models in the resources at configured locations: {}", locations);

        DeploymentBuilder builder = repositoryService.createDeployment()
                .name(MICRONAUT_AUTO_DEPLOYMENT_NAME)
                .enableDuplicateFiltering(true);

        boolean deploy = false;
        for (String extension : Arrays.asList("dmn", "bpmn", "form")) {
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

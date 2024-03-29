/*
 * Copyright 2021-2022 original authors
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
package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.initialization.DeployModelsExecutor
import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.camunda.bpm.engine.ManagementService
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.repository.Deployment
import org.camunda.bpm.engine.repository.ProcessDefinition
import org.camunda.bpm.engine.repository.Resource
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.stream.Collectors

class ProcessEngineFactoryDeploymentTest {

    @MicronautTest
    @Nested
    inner class SimpleDeployment {

        @Inject
        lateinit var repositoryService: RepositoryService

        @Inject
        lateinit var managementService: ManagementService

        @Test
        fun `deployment name is set`() {
            assertThat(repositoryService.createDeploymentQuery().list())
                .extracting<String>(Deployment::getName)
                .contains(DeployModelsExecutor.MICRONAUT_AUTO_DEPLOYMENT_NAME)
        }

        @Test
        fun `camunda forms get deployed`() {
            assertThat(managementService.registeredDeployments.stream()
                .map { deploymentId -> repositoryService.getDeploymentResources(deploymentId) }
                .flatMap { obj -> obj.stream() }
                .map(Resource::getName)
                .collect(Collectors.toList())).contains("sample.form")
        }

        @Test
        fun `DMN tables get deployed`() {
            assertThat(managementService.registeredDeployments.stream()
                .map { deploymentId -> repositoryService.getDeploymentResources(deploymentId) }
                .flatMap { obj -> obj.stream() }
                .map(Resource::getName)
                .collect(Collectors.toList())).contains("simple-table.dmn")
        }
    }

    @MicronautTest
    @Property(name="camunda.locations", value="classpath:.,classpath:bpm")
    @Nested
    inner class Subdirectory {

        @Inject
        lateinit var repositoryService: RepositoryService

        @Test
        fun `all given locations are scanned`() {
            assertThat(repositoryService.createProcessDefinitionQuery().latestVersion().list())
                .extracting<String>(ProcessDefinition::getKey)
                .contains("ProcessEmpty", "ProcessEmptySubdir")
        }

    }
}
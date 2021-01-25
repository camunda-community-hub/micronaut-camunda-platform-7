package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.ProcessEngineFactory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.RepositoryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ProcessEngineFactoryTest {
    @Inject
    lateinit var processEngine: ProcessEngine

    @Inject
    lateinit var repositoryService: RepositoryService

    @Test
    fun `processEngine available in ApplicationContext`() {
        assertNotNull(processEngine)
    }

    @Test
    fun `auto deployment sets deployment name`() {
        assertEquals(1, repositoryService.createDeploymentQuery()
            .deploymentName(ProcessEngineFactory.MICRONAUT_AUTO_DEPLOYMENT_NAME)
            .list()
            .size)
    }

    @Test
    fun `configuration customizer`() {
        assertEquals(MicronautProcessEngineConfigurationCustomizer.PROCESS_ENGINE_NAME, processEngine.name)
    }
}
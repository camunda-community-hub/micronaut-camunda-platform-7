package info.novatec.micronaut.camunda.bpm.feature.test;

import info.novatec.micronaut.camunda.bpm.feature.ProcessEngineFactory;
import io.micronaut.test.annotation.MicronautTest;
import org.camunda.bpm.engine.*;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class ProcessEngineFactoryTest {

    @Inject
    ProcessEngine processEngine;

    @Test
    void processEngineAvailableInApplicationContext() {
        assertNotNull(processEngine);
    }

    @Test
    void testDeploymentName() {
        assertEquals(ProcessEngineFactory.MICRONAUT_AUTO_DEPLOYMENT_NAME, processEngine.getRepositoryService().createDeploymentQuery().singleResult().getName());
    }

    @Test
    void testConfigurationCustomizer() {
        assertEquals(MicronautProcessEngineConfigurationCustomizer.PROCESS_ENGINE_NAME, processEngine.getName());
    }
}

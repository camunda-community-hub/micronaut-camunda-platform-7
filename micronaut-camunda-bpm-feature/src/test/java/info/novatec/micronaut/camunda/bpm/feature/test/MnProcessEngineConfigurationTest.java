package info.novatec.micronaut.camunda.bpm.feature.test;

import info.novatec.micronaut.camunda.bpm.feature.MnAbstractProcessEngineConfiguration;
import info.novatec.micronaut.camunda.bpm.feature.MnStandaloneProcessEngineConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tobias Schäfer
 */
@MicronautTest
class MnProcessEngineConfigurationTest {

    @Inject
    MnAbstractProcessEngineConfiguration processEngineConfiguration;

    @Test
    void testMicronautProcessEngineConfigurationClass() {
        assertEquals(MnStandaloneProcessEngineConfiguration.class, processEngineConfiguration.getClass());;
    }
}
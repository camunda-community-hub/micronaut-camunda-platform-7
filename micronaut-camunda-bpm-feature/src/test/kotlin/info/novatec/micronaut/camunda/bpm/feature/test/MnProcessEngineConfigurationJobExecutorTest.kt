package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.ApplicationContext
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * @author Tobias Schäfer
 */
class MnProcessEngineConfigurationJobExecutorTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `job executor is enabled for production but not for tests`(test : Boolean) {
        ApplicationContext.builder().deduceEnvironment(test).build().start().use { applicationContext ->
            val pec = applicationContext.getBean(ProcessEngine::class.java).processEngineConfiguration as ProcessEngineConfigurationImpl
            assertEquals(!test, pec.isJobExecutorActivate)
        }
    }
}

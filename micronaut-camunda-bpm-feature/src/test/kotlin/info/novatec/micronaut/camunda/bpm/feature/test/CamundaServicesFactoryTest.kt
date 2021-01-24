package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class CamundaServicesFactoryTest {
    @Inject
    lateinit var runtimeService: RuntimeService

    @Inject
    lateinit var repositoryService: RepositoryService

    @Inject
    lateinit var managementService: ManagementService

    @Inject
    lateinit var authorizationService: AuthorizationService

    @Inject
    lateinit var caseService: CaseService

    @Inject
    lateinit var decisionService: DecisionService

    @Inject
    lateinit var externalTaskService: ExternalTaskService

    @Inject
    lateinit var filterService: FilterService

    @Inject
    lateinit var formService: FormService

    @Inject
    lateinit var taskService: TaskService

    @Inject
    lateinit var historyService: HistoryService

    @Inject
    lateinit var identityService: IdentityService

    @Test
    fun `all service beans are available in applicationContext`() {
        assertNotNull(runtimeService)
        assertNotNull(repositoryService)
        assertNotNull(managementService)
        assertNotNull(authorizationService)
        assertNotNull(caseService)
        assertNotNull(decisionService)
        assertNotNull(externalTaskService)
        assertNotNull(filterService)
        assertNotNull(formService)
        assertNotNull(taskService)
        assertNotNull(historyService)
        assertNotNull(identityService)
    }
}
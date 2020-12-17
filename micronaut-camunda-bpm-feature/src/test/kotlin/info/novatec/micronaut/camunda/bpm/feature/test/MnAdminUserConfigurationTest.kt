package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import info.novatec.micronaut.camunda.bpm.feature.MnAdminUserConfiguration
import io.micronaut.context.ApplicationContext
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.core.value.PropertyNotFoundException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.authorization.Authorization.ANY
import org.camunda.bpm.engine.authorization.Groups.CAMUNDA_ADMIN
import org.camunda.bpm.engine.authorization.Groups.GROUP_TYPE_SYSTEM
import org.camunda.bpm.engine.authorization.Resources
import org.camunda.bpm.engine.identity.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

/**
 * Tests for [info.novatec.micronaut.camunda.bpm.feature.MnAdminUserConfiguration]
 *
 * @author Titus Meyer
 */
class MnAdminUserConfigurationTest {
    @MicronautTest
    @Nested
    inner class MnAdminUserConfigurationTestWithoutProperties {
        @Inject
        lateinit var processEngine: ProcessEngine

        @Inject
        lateinit var configuration: Configuration

        @Test
        fun adminUserNotDefined() {
            assertThrows(PropertyNotFoundException::class.java) { configuration.adminUser.id }
            assertEquals(0, processEngine.identityService.createUserQuery().count())
        }
    }

    @MicronautTest(propertySources = ["classpath:adminusertest.yml"])
    @Nested
    inner class MnAdminUserConfigurationTestWithProperties {
        @Inject
        lateinit var processEngine: ProcessEngine

        @Inject
        lateinit var configuration: Configuration

        @Inject
        lateinit var mnAdminUserConfiguration: Optional<MnAdminUserConfiguration>

        @Inject
        lateinit var applicationContext: ApplicationContext

        @Test
        fun adminUserCreated() {
            assertTrue(mnAdminUserConfiguration.isPresent)

            assertEquals("admin", configuration.adminUser.id)
            assertEquals("password", configuration.adminUser.password)
            assertEquals("Firstname", configuration.adminUser.firstname)
            assertEquals("Lastname", configuration.adminUser.lastname)

            assertAdminUserExists(processEngine, configuration.adminUser.id)
            assertAdminGroupExists(processEngine)
            assertAdminGroupAuthorizationsExist(processEngine)
        }

        @Test
        fun adminUserCreationCalledAgain() {
            val event: BeanCreatedEvent<ProcessEngine> = BeanCreatedEvent(applicationContext, null, null, processEngine)

            mnAdminUserConfiguration.get().onCreated(event)

            assertEquals(1, processEngine.identityService.createUserQuery().count())
            assertAdminUserExists(processEngine, configuration.adminUser.id)
            assertAdminGroupExists(processEngine)
            assertAdminGroupAuthorizationsExist(processEngine)
        }

        fun queryUser(processEngine: ProcessEngine, userId: String): User {
            return processEngine.identityService.createUserQuery().userId(userId).singleResult()
        }

        fun assertAdminUserExists(processEngine: ProcessEngine, userId: String) {
            val adminUser = queryUser(processEngine, userId)
            assertNotNull(adminUser)
            assertEquals(userId, adminUser.id)
        }

        fun assertAdminGroupExists(processEngine: ProcessEngine) {
            val adminGroup = processEngine.identityService.createGroupQuery().groupId(CAMUNDA_ADMIN).singleResult()
            assertNotNull(adminGroup)
            assertEquals(GROUP_TYPE_SYSTEM, adminGroup.type)
        }

        fun assertAdminGroupAuthorizationsExist(processEngine: ProcessEngine) {
            for (resource in Resources.values()) {
                assertEquals(
                    1,
                    processEngine.authorizationService.createAuthorizationQuery()
                        .groupIdIn(CAMUNDA_ADMIN).resourceType(resource).resourceId(ANY).count()
                )
            }
        }
    }
}
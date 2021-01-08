package info.novatec.micronaut.camunda.bpm.feature.test

import info.novatec.micronaut.camunda.bpm.feature.Configuration
import info.novatec.micronaut.camunda.bpm.feature.AdminUserCreator
import io.micronaut.core.value.PropertyNotFoundException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.authorization.Authorization.ANY
import org.camunda.bpm.engine.authorization.Groups.CAMUNDA_ADMIN
import org.camunda.bpm.engine.authorization.Groups.GROUP_TYPE_SYSTEM
import org.camunda.bpm.engine.authorization.Resources
import org.camunda.bpm.engine.identity.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject

/**
 * Tests for [info.novatec.micronaut.camunda.bpm.feature.AdminUserCreator]
 *
 * @author Titus Meyer
 */
class AdminUserCreatorTest {
    @MicronautTest
    @Nested
    inner class AdminUserCreatorTestWithoutProperties {
        @Inject
        lateinit var processEngine: ProcessEngine

        @Inject
        lateinit var configuration: Configuration

        @Inject
        lateinit var adminUserCreator: Optional<AdminUserCreator>

        @Test
        fun adminUserNotDefined() {
            assertThrows(PropertyNotFoundException::class.java) { configuration.adminUser.id }
            assertFalse(adminUserCreator.isPresent)
            assertEquals(0, processEngine.identityService.createUserQuery().count())
        }
    }

    @MicronautTest
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class AdminUserCreatorTestWithProperties : TestPropertyProvider {

        override fun getProperties(): MutableMap<String, String> {
            return mutableMapOf(
                "camunda.bpm.admin-user.id" to "admin",
                "camunda.bpm.admin-user.password" to "password",
                "camunda.bpm.admin-user.firstname" to "Firstname",
                "camunda.bpm.admin-user.lastname" to "Lastname",
            )
        }

        @Inject
        lateinit var processEngine: ProcessEngine

        @Inject
        lateinit var configuration: Configuration

        @Inject
        lateinit var adminUserCreator: Optional<AdminUserCreator>

        @Test
        fun adminUserCreated() {
            assertTrue(adminUserCreator.isPresent)

            triggerServerStartupEvent(adminUserCreator.get())

            assertEquals("admin", configuration.adminUser.id)
            assertEquals("password", configuration.adminUser.password)
            assertEquals("Firstname", configuration.adminUser.firstname)
            assertEquals("Lastname", configuration.adminUser.lastname)

            assertAdminUserExists(processEngine, configuration.adminUser.id)
            assertAdminGroupExists(processEngine)
            assertAdminGroupAuthorizationsExist(processEngine)
        }

        @Test
        fun adminUserOnlyCreatedOnce() {
            assertTrue(adminUserCreator.isPresent)

            triggerServerStartupEvent(adminUserCreator.get())
            triggerServerStartupEvent(adminUserCreator.get())

            assertEquals(1, processEngine.identityService.createUserQuery().count())
            assertAdminUserExists(processEngine, configuration.adminUser.id)
            assertAdminGroupExists(processEngine)
            assertAdminGroupAuthorizationsExist(processEngine)
        }

        /**
         * Provide method to trigger event manually because we don't have an application in the feature project to fire the event
         */
        fun triggerServerStartupEvent(adminUserCreator: AdminUserCreator) {
            adminUserCreator.onApplicationEvent(ServerStartupEvent(mock(EmbeddedServer::class.java)))
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
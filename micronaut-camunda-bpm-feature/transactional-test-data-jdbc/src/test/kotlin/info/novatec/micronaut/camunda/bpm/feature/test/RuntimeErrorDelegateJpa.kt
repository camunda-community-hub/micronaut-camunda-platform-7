package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.annotation.Replaces
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import javax.inject.Named
import javax.inject.Singleton

/**
 * [JavaDelegate] which replaces @[RuntimeErrorDelegate] which first saves a new entity and then throws a [RuntimeException].
 *
 * In a transactional context the persistence of the entity should be rolled back.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Replaces(RuntimeErrorDelegate::class)
@Named("runtimeErrorDelegate")
class RuntimeErrorDelegateJpa(private val bookRepository: BookRepository) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        bookRepository.save(Book(execution.businessKey))
        throw RuntimeException("RuntimeErrorDelegate throws RuntimeException")
    }
}
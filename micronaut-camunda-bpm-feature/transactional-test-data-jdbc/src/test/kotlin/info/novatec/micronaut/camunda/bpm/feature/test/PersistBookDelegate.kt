package info.novatec.micronaut.camunda.bpm.feature.test

import io.micronaut.context.annotation.Replaces
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import javax.inject.Named
import javax.inject.Singleton

/**
 * [JavaDelegate] which replaces the [DoNothingDelegate] and included the persistence of an entity.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Replaces(DoNothingDelegate::class)
@Named("doSomethingDelegate")
class PersistBookDelegate(private val bookRepository: BookRepository) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        bookRepository.save(Book(execution.businessKey))
    }
}
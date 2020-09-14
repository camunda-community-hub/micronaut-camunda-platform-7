package info.novatec.micronaut.camunda.bpm.feature.test;

import io.micronaut.context.annotation.Replaces;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * {@link JavaDelegate} which replaces @{@link RuntimeErrorDelegate} which first saves a new entity and then throws a {@link RuntimeException}.
 *
 * In a transactional context the persistence of the entity should be rolled back.
 *
 * @author Tobias Schäfer
 */
@Singleton
@Replaces(RuntimeErrorDelegate.class)
@Named("runtimeErrorDelegate")
public class RuntimeErrorDelegateJpa implements JavaDelegate {

    private final BookRepository bookRepository;

    public RuntimeErrorDelegateJpa(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute(DelegateExecution execution) {
        bookRepository.save(new Book());
        throw new RuntimeException("RuntimeErrorDelegate throws RuntimeException");
    }
}

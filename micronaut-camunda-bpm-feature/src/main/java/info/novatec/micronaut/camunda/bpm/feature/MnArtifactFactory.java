package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.exceptions.NoSuchBeanException;
import org.camunda.bpm.engine.ArtifactFactory;
import org.camunda.bpm.engine.impl.DefaultArtifactFactory;

import javax.inject.Singleton;

/**
 * Micronaut specific implementation of {@link ArtifactFactory} to resolve classes from the {@link ApplicationContext}.
 *
 * @author Tobias Schäfer
 */
@Singleton
public class MnArtifactFactory implements ArtifactFactory {

    private final ApplicationContext applicationContext;

    private final ArtifactFactory defaultArtifactFactory = new DefaultArtifactFactory();

    public MnArtifactFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getArtifact(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanException ex) {
            // fall back to default implementation
            return defaultArtifactFactory.getArtifact(clazz);
        }
    }
}

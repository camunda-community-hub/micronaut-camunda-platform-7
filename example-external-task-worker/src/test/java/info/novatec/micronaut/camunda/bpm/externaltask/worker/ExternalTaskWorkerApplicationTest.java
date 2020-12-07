package info.novatec.micronaut.camunda.bpm.externaltask.worker;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class ExternalTaskWorkerApplicationTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        assertTrue(application.isRunning());
    }

}

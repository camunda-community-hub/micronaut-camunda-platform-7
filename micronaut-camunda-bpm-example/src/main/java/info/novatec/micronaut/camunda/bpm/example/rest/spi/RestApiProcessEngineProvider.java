package info.novatec.micronaut.camunda.bpm.example.rest.spi;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;

import java.util.Set;

/**
 * This class is for wiring of the REST API and the process engine.
 *
 * See also https://docs.camunda.org/manual/current/reference/rest/overview/embeddability/
 *
 * @author Martin Sawilla
 */
//Implementation based on Spring-Boot-Starter: https://github.com/camunda/camunda-bpm-spring-boot-starter/blob/master/starter-rest/src/main/java/org/camunda/bpm/spring/boot/starter/rest/spi/SpringBootProcessEngineProvider.java
public class RestApiProcessEngineProvider implements ProcessEngineProvider {

    @Override
    public ProcessEngine getDefaultProcessEngine() {
        return ProcessEngines.getDefaultProcessEngine();
    }

    @Override
    public ProcessEngine getProcessEngine(String name) {
        return ProcessEngines.getProcessEngine(name);
    }

    @Override
    public Set<String> getProcessEngineNames() {
        return ProcessEngines.getProcessEngines().keySet();
    }
}
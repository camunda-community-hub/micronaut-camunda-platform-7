package info.novatec.micronaut.camunda.bpm.example;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ResourceDefinition;

import java.util.stream.Collectors;

@Controller("/example")
public class CamundaController {

    private final ProcessEngine processEngine;

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    public CamundaController(ProcessEngine processEngine, RepositoryService repositoryService, RuntimeService runtimeService) {
        this.processEngine = processEngine;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
    }

    @Get("/name")
    @Produces(MediaType.TEXT_PLAIN)
    @ExecuteOn(TaskExecutors.IO)
    public String name() {
        return processEngine.getName();
    }

    @Get("/definitions")
    @Produces(MediaType.TEXT_PLAIN)
    @ExecuteOn(TaskExecutors.IO)
    public String definitions() {
        return repositoryService.createProcessDefinitionQuery().list().stream()
                .map(ResourceDefinition::getKey)
                .sorted()
                .collect(Collectors.joining(","));
    }

    @Post("/hello-world-process")
    @ExecuteOn(TaskExecutors.IO)
    public String startHelloWorldProcess() {
        return runtimeService.startProcessInstanceByKey("HelloWorld").getId();
    }

}
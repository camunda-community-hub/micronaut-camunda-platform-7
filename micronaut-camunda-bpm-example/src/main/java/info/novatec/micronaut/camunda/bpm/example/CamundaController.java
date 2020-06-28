package info.novatec.micronaut.camunda.bpm.example;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ResourceDefinition;

import java.util.stream.Collectors;

@Controller("/camunda")
public class CamundaController {

    private final ProcessEngine processEngine;

    private final RepositoryService repositoryService;

    public CamundaController(ProcessEngine processEngine, RepositoryService repositoryService) {
        this.processEngine = processEngine;
        this.repositoryService = repositoryService;
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
                .collect(Collectors.joining());
    }
}
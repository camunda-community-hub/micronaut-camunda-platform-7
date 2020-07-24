package info.novatec.micronaut.camunda.externaltask.process;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micronaut.http.annotation.*;
import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.ExternalTaskQuery;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryBuilder;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.externaltask.*;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.variable.VariableMap;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response.Status;

@Controller("/external-task")
public class MyExternalTaskRestService //implements ExternalTaskRestService
{

    //TODO: inject UriInfo?

    @Inject
    ProcessEngine processEngine;

    @Get
    public List<ExternalTaskDto> ets() {
        ExternalTaskQueryDto queryDto = new ExternalTaskQueryDto();
        queryDto.setObjectMapper(new ObjectMapper());
        ExternalTaskQuery query = queryDto.toQuery(processEngine);

        List<ExternalTask> matchingTasks = query.list();

        List<ExternalTaskDto> taskResults = new ArrayList<>();
        for (ExternalTask task : matchingTasks) {
            ExternalTaskDto resultInstance = ExternalTaskDto.fromExternalTask(task);
            taskResults.add(resultInstance);
        }
        return taskResults;
    }

    @Get("/count")
    public CountResultDto et() {
        ExternalTaskQueryDto queryDto = new ExternalTaskQueryDto();
        queryDto.setObjectMapper(new ObjectMapper());
        ExternalTaskQuery query = queryDto.toQuery(processEngine);

        return new CountResultDto(query.count());
    }

    @Post("/fetchAndLock")
    public List<LockedExternalTaskDto> fetchAndLock(FetchExternalTasksDto fetchingDto) {
        ExternalTaskQueryBuilder fetchBuilder = fetchingDto.buildQuery(processEngine);
        List<LockedExternalTask> externalTasks = fetchBuilder.execute();
        return LockedExternalTaskDto.fromLockedExternalTasks(externalTasks);
    }

    @Post("/{externalTaskId}/complete")
    public void complete(@PathVariable String externalTaskId, @Body CompleteExternalTaskDto dto) {
        ExternalTaskService externalTaskService = processEngine.getExternalTaskService();

        VariableMap variables = VariableValueDto.toMap(dto.getVariables(), processEngine, new ObjectMapper());

        try {
            externalTaskService.complete(externalTaskId, dto.getWorkerId(), variables);
        } catch (NotFoundException e) {
            throw new RestException(Status.NOT_FOUND, e, "External task with id " + externalTaskId + " does not exist");
        } catch (BadUserRequestException e) {
            throw new RestException(Status.BAD_REQUEST, e, e.getMessage());
        }
    }

}

package info.novatec.micronaut.camunda.bpm.example.onboarding;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.camunda.bpm.engine.RuntimeService;

@Controller("/example/onboarding")
public class OnboardingController {

    private final RuntimeService runtimeService;

    public OnboardingController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Post("/cancel/{businessKey}")
    @ExecuteOn(TaskExecutors.IO)
    public String customerCancellation(@PathVariable String businessKey) {
        runtimeService.correlateMessage("MessageCustomerCancellation", businessKey);
        return "Cancelled: " + businessKey;
    }
}

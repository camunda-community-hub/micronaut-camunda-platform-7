package info.novatec.micronaut.camunda.bpm.example.onboarding;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.camunda.bpm.engine.RuntimeService;

import javax.inject.Singleton;

@Singleton
public class OnboardingProcessInstanceCreator implements ApplicationEventListener<ServerStartupEvent> {

    private final RuntimeService runtimeService;

    public OnboardingProcessInstanceCreator(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    @ExecuteOn(TaskExecutors.IO)
    public void onApplicationEvent(ServerStartupEvent event) {
        runtimeService.startProcessInstanceByKey("Onboarding", "OnStartup");
    }
}

package info.novatec.micronaut.camunda.bpm.example;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/greet")
public class GreetController {

    private final GreetingService greetingService;

    public GreetController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Get("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String greet(String name) {
        return greetingService.getGreeting(name);
    }
}
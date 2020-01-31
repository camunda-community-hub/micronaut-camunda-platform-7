package micronaut.camunda.app;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import javax.inject.Inject;

@Controller("/greet")
public class GreetController {

    @Inject
    private GreetingService greetingService;

    @Get("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String greet(String name) {
        return greetingService.getGreeting(name);
    }
}
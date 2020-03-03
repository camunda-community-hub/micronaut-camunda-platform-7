package info.novatec.micronaut.camunda.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class GreetControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void greet() {
        HttpRequest<String> request = HttpRequest.GET("/greet/Tobias");
        String body = client.toBlocking().retrieve(request);

        assertEquals("Hello Tobias", body);
    }
}

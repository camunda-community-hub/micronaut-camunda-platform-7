package info.novatec.micronaut.camunda.bpm.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class CamundaControllerTest {

    @Inject
    @Client("/example")
    RxHttpClient client;

    @Test
    void name() {
        HttpRequest<String> request = HttpRequest.GET("/name");
        String body = client.toBlocking().retrieve(request);

        assertEquals("default", body);
    }

    @Test
    void definitions() {
        HttpRequest<String> request = HttpRequest.GET("/definitions");
        String body = client.toBlocking().retrieve(request);

        assertEquals("HelloWorld,Onboarding", body);
    }
}

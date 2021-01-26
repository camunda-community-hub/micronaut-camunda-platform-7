package info.novatec.micronaut.camunda.bpm.feature.webapp;

import info.novatec.micronaut.camunda.bpm.feature.Configuration;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.net.URI;

/***
 * @author Martin Sawilla
 */
@Requires(property = "camunda.bpm.webapps.index-redirect-enabled", notEquals = "false", defaultValue = "true")
@Controller
public class RedirectToWebappsController {
    protected final Configuration configuration;

    public RedirectToWebappsController(Configuration configuration) {
        this.configuration = configuration;
    }

    @Get
    public HttpResponse<?> redirect () {
        return HttpResponse.temporaryRedirect(URI.create(configuration.getWebapps().getContextPath()));
    }
}

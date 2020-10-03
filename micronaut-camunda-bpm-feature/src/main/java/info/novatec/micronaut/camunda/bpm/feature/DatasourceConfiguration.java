package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.bind.annotation.Bindable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Tobias Schäfer
 */
@ConfigurationProperties("datasources.default")
public interface DatasourceConfiguration {

    @Bindable(defaultValue = "jdbc:h2:mem:default;DB_CLOSE_ON_EXIT=FALSE")
    @NotBlank
    String getUrl();

    @Bindable(defaultValue = "sa")
    @NotBlank
    String getUsername();

    @Bindable(defaultValue = "")
    @NotNull
    String getPassword();

    @Bindable(defaultValue = "org.h2.Driver")
    @NotBlank
    String getDriverClassName();
}
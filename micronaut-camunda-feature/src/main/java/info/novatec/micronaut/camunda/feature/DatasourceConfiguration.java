package info.novatec.micronaut.camunda.feature;

import io.micronaut.aop.Introduction;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Type;
import io.micronaut.core.bind.annotation.Bindable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ConfigurationProperties("datasources.default")
//@Introduction
//@Retention(RetentionPolicy.RUNTIME)
//@Type(ElementType.TYPE)
public interface DatasourceConfiguration {

    @Bindable(defaultValue = "jdbc:h2:mem:micronaut-db;DB_CLOSE_DELAY=1000")
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
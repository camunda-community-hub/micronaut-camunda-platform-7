package info.novatec.micronaut.camunda.feature;

import io.micronaut.aop.Introduction;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.bind.annotation.Bindable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@ConfigurationProperties("camunda.bpm")
//@Introduction
//@Retention(RetentionPolicy.RUNTIME)
//@Type(ElementType.TYPE)
public interface Configuration {

    @NotNull
    Database getDatabase();

    @ConfigurationProperties("database")
    //@Introduction
    //@Retention(RetentionPolicy.RUNTIME)
    //@Type(ElementType.TYPE)
    interface Database {
        @NotBlank
        @Bindable(defaultValue = "true")
        String getSchemaUpdate();
    }
}

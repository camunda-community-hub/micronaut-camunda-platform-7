package info.novatec.micronaut.camunda.externaltask.worker;

import java.lang.annotation.*;
import java.util.Optional;

import static java.lang.annotation.RetentionPolicy.*;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
public @interface ExternalTaskSubscription {
    String topic();

    String lockDuration() default "";
}

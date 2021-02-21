package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.ProcessEngine;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * @author Paty Alonso
 */

//Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/util/CamundaBpmVersion.java
@Singleton
public class CamundaVersion {

    private final Optional<String> version;

    public CamundaVersion() {
        this(ProcessEngine.class.getPackage());
    }

    protected CamundaVersion(Package pkg) {
        version = Optional.ofNullable(pkg.getImplementationVersion())
                .map(String::trim);
    }

    public Optional<String> getVersion() {
        return version;
    }
}

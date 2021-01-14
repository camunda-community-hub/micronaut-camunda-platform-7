package info.novatec.micronaut.camunda.bpm.feature;

import org.camunda.bpm.engine.ProcessEngine;

import javax.inject.Singleton;
import java.util.Optional;

/**
 * @author Paty Alonso
 */

//Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/spring-boot-starter/starter/src/main/java/org/camunda/bpm/spring/boot/starter/util/CamundaBpmVersion.java
@Singleton
public class CamundaBpmVersion {

    private final String version;

    public CamundaBpmVersion() {
        this(ProcessEngine.class.getPackage());
    }

    protected CamundaBpmVersion(Package pkg) {
        version = Optional.ofNullable(pkg.getImplementationVersion())
                .map(String::trim)
                .orElseThrow(() -> new IllegalStateException("Could not determine version"));
    }

    public String getVersion() {
        return version;
    }
}

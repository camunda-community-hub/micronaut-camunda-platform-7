package info.novatec.micronaut.camunda.bpm.feature;

/**
 * @author Aleksandr Arshavskiy
 */
@FunctionalInterface
public interface JobExecutorCustomizer {

    void customize(MnJobExecutor jobExecutor);

}

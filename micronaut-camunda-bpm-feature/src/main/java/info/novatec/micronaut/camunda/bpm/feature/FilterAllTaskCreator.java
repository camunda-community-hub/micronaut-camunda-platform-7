package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * Create a simple "show all" filter if {@code camunda.bpm.filter.create} property is present.
 *
 * @author Martin Sawilla
 */
// Implementation based on https://github.com/camunda/camunda-bpm-spring-boot-starter/blob/master/starter/src/main/java/org/camunda/bpm/spring/boot/starter/configuration/impl/custom/CreateFilterConfiguration.java
@Singleton
@Requires(property = "camunda.bpm.filter.create")
public class FilterAllTaskCreator implements ApplicationEventListener<ServerStartupEvent> {
    private static final Logger log = LoggerFactory.getLogger(FilterAllTaskCreator.class);

    protected final ProcessEngine processEngine;

    protected String filterName;

    public FilterAllTaskCreator(ProcessEngine processEngine, Configuration configuration) {
        this.processEngine = processEngine;
        filterName = configuration.getFilter().getCreate()
                .orElseThrow(() -> new IllegalArgumentException("If property 'camunda.bpm.filter.create' is set it must have a value."));
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        Filter filter = processEngine.getFilterService().createFilterQuery().filterName(filterName).singleResult();
        if (filter == null) {
            filter = processEngine.getFilterService().newTaskFilter(filterName);
            processEngine.getFilterService().saveFilter(filter);
            log.info("Created new task filter: {}", filterName);
        }
    }
}

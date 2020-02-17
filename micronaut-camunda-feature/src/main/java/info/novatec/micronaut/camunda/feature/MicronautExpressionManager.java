package info.novatec.micronaut.camunda.feature;

import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.el.ReadOnlyMapELResolver;
import org.camunda.bpm.engine.impl.el.VariableContextElResolver;
import org.camunda.bpm.engine.impl.el.VariableScopeElResolver;
import org.camunda.bpm.engine.impl.javax.el.*;

// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/engine-spring/core/src/main/java/org/camunda/bpm/engine/spring/SpringExpressionManager.java
public class MicronautExpressionManager extends ExpressionManager {

    private final ApplicationContextElResolver applicationContextElResolver;

    public MicronautExpressionManager(ApplicationContextElResolver applicationContextElResolver) {
        this.applicationContextElResolver = applicationContextElResolver;
    }

    @Override
    protected ELResolver createElResolver() {
        CompositeELResolver compositeElResolver = new CompositeELResolver();
        compositeElResolver.add(new VariableScopeElResolver());
        compositeElResolver.add(new VariableContextElResolver());

        if(beans != null) {
            // Only expose limited set of beans in expressions
            compositeElResolver.add(new ReadOnlyMapELResolver(beans));
        } else {
            // Expose full application-context in expressions
            compositeElResolver.add(applicationContextElResolver);
        }

        compositeElResolver.add(new ArrayELResolver());
        compositeElResolver.add(new ListELResolver());
        compositeElResolver.add(new MapELResolver());
        compositeElResolver.add(new BeanELResolver());

        return compositeElResolver;
    }

}

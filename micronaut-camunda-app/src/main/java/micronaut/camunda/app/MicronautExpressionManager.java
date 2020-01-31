package micronaut.camunda.app;

import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.el.ReadOnlyMapELResolver;
import org.camunda.bpm.engine.impl.el.VariableContextElResolver;
import org.camunda.bpm.engine.impl.el.VariableScopeElResolver;
import org.camunda.bpm.engine.impl.javax.el.*;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MicronautExpressionManager extends ExpressionManager {

    @Inject
    private ApplicationContextElResolver applicationContextElResolver;

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

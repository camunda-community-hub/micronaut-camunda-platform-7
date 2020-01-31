package micronaut.camunda.app;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.Qualifier;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.javax.el.ELContext;
import org.camunda.bpm.engine.impl.javax.el.ELResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

@Singleton
public class ApplicationContextElResolver extends ELResolver {

    @Inject
    private ApplicationContext applicationContext;

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (base == null) {
            // according to javadoc, can only be a String
            String key = (String) property;

            Qualifier qualifier = new MyNameQualifier<>(key);
            if (applicationContext.containsBean(Object.class, qualifier)) {
                context.setPropertyResolved(true);
                return applicationContext.getBean (Object.class, qualifier);
            }
        }

        return null;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (base == null) {
            String key = (String) property;
            if (applicationContext.containsBean(Object.class, new MyNameQualifier<>(key))) {
                throw new ProcessEngineException("Cannot set value of '" + property +
                        "', it resolves to a bean defined in the Micronaut application-context.");
            }
        }
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object arg) {
        return Object.class;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object arg) {
        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object arg1, Object arg2) {
        return Object.class;
    }
}

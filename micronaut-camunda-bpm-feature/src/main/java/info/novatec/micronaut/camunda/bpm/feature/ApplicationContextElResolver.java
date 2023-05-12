/*
 * Copyright 2020-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.micronaut.camunda.bpm.feature;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.Qualifier;
import io.micronaut.inject.qualifiers.Qualifiers;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.impl.juel.jakarta.el.ELContext;
import org.camunda.bpm.impl.juel.jakarta.el.ELResolver;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

/**
 * @author Tobias Schäfer
 */
// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/engine-spring/core/src/main/java/org/camunda/bpm/engine/spring/ApplicationContextElResolver.java
public class ApplicationContextElResolver extends ELResolver {

    protected final ApplicationContext applicationContext;

    public ApplicationContextElResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (base == null) {
            // according to javadoc, can only be a String
            String key = (String) property;

            Qualifier<Object> qualifier = Qualifiers.byName(key);
            if (applicationContext.containsBean(Object.class, qualifier)) {
                context.setPropertyResolved(true);
                return applicationContext.getBean(Object.class, qualifier);
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
            if (applicationContext.containsBean(Object.class, Qualifiers.byName(key))) {
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

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

import org.camunda.bpm.engine.impl.el.*;
import org.camunda.bpm.impl.juel.jakarta.el.*;

/**
 * @author Tobias Schäfer
 */
// Implementation based on https://github.com/camunda/camunda-bpm-platform/blob/master/engine-spring/core/src/main/java/org/camunda/bpm/engine/spring/SpringExpressionManager.java
public class MnExpressionManager extends JuelExpressionManager {

    protected final ApplicationContextElResolver applicationContextElResolver;

    public MnExpressionManager(ApplicationContextElResolver applicationContextElResolver) {
        this.applicationContextElResolver = applicationContextElResolver;
    }

    @Override
    protected ELResolver createElResolver() {
        CompositeELResolver compositeElResolver = new CompositeELResolver();
        compositeElResolver.add(new VariableScopeElResolver());
        compositeElResolver.add(new VariableContextElResolver());

        if (beans != null) {
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

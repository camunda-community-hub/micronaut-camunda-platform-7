/*
 * Copyright 2022 original authors
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

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator;

/**
 * @author Tobias Schäfer
 */
// Implementation based on https://github.com/camunda/camunda-bpm-spring-boot-starter/blob/master/starter/src/main/java/org/camunda/bpm/spring/boot/starter/configuration/id/IdGeneratorConfiguration.java
@Factory
public class IdGeneratorFactory {

    @Singleton
    public IdGenerator idGenerator() {
        return new StrongUuidGenerator();
    }
}

/*
 * Copyright 2021 original authors
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
package info.novatec.external.task.worker.feature;

import io.micronaut.context.annotation.Factory;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.ExternalTaskClientBuilder;
import org.camunda.bpm.client.impl.ExternalTaskClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * @author Martin Sawilla
 */
@Factory
public class ExternalTaskClientFactory {

    private static Logger log = LoggerFactory.getLogger(ExternalTaskClientFactory.class);

    @Singleton
    ExternalTaskClient buildClient(Configuration configuration, ExternalClientCustomizer externalClientCustomizer) {

        ExternalTaskClientBuilder clientBuilder = ExternalTaskClient.create();

        externalClientCustomizer.customize(clientBuilder);

        configuration.getBaseUrl().ifPresent(it -> clientBuilder.baseUrl(it));
        configuration.getWorkerId().ifPresent(it -> clientBuilder.workerId(it));
        configuration.getMaxTasks().ifPresent(it -> clientBuilder.maxTasks(it));
        configuration.getUsePriority().ifPresent(it -> clientBuilder.usePriority(it));
        configuration.getDefaultSerializationFormat().ifPresent(it -> clientBuilder.defaultSerializationFormat(it));
        configuration.getDateFormat().ifPresent(it -> clientBuilder.dateFormat(it));
        configuration.getAsyncResponseTimeout().ifPresent(it -> clientBuilder.asyncResponseTimeout(it));
        configuration.getLockDuration().ifPresent(it -> clientBuilder.lockDuration(it));
        // TODO Disable auto fetching?
        configuration.getDisableBackoffStrategy().ifPresent(it -> {
            if(it) {
                clientBuilder.disableBackoffStrategy();
            }
        });

        ExternalTaskClient client = clientBuilder.build();

        final String baseUrl = ((ExternalTaskClientImpl) client).getTopicSubscriptionManager().getEngineClient().getBaseUrl();
        log.info("ExternalTaskClient connected to {} and ready to process tasks", baseUrl);

        return client;
    }
}

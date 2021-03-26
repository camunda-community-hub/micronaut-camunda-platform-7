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

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.BeanDefinition;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Sawilla
 *
 * Allows to configure an externa task worker with the ExternalTaskSubscription annotation. This allows to easily build
 * External workers for multiple topics.
 */
@Context
public class ExternalWorkerSubscriptionCreator {

    private static final Logger log = LoggerFactory.getLogger(ExternalWorkerSubscriptionCreator.class);

    protected final Configuration configuration;
    protected final BeanContext beanContext;
    protected final ExternalTaskClient externalTaskClient;

    public ExternalWorkerSubscriptionCreator(Configuration configuration,
                                             BeanContext beanContext,
                                             ExternalTaskClient externalTaskClient) {
        this.configuration = configuration;
        this.beanContext = beanContext;
        this.externalTaskClient = externalTaskClient;

        beanContext.getBeanDefinitions(ExternalTaskHandler.class).forEach(this::registerExternalTaskHandler);
    }

    protected void registerExternalTaskHandler(BeanDefinition<ExternalTaskHandler> beanDefinition) {
        ExternalTaskHandler externalTaskHandler = beanContext.getBean(beanDefinition);
        AnnotationValue<ExternalTaskSubscription> annotationValue = beanDefinition.getAnnotation(ExternalTaskSubscription.class);

        if (annotationValue != null) {
            buildTopicSubscription(externalTaskHandler, externalTaskClient, annotationValue);
        } else {
            log.warn("Skipping subscription. Could not find Annotation ExternalTaskSubscription on class {}", beanDefinition.getName());
        }
    }

    protected void buildTopicSubscription(ExternalTaskHandler externalTaskHandler, ExternalTaskClient client, AnnotationValue<ExternalTaskSubscription> annotationValue) {

        TopicSubscriptionBuilder builder = client.subscribe(annotationValue.stringValue("topicName").get());;

        builder.handler(externalTaskHandler);

        annotationValue.longValue("lockDuration").ifPresent(it -> {
            builder.lockDuration(it);
        });

        annotationValue.get("variables", String[].class).ifPresent(it -> {
            if(!it[0].equals("")){
                builder.variables(it);
            }
        });

        annotationValue.booleanValue("localVariables").ifPresent(it -> {
            builder.localVariables(it);
        });

        annotationValue.stringValue("businessKey").ifPresent(it -> {
            builder.businessKey(it);
        });

        annotationValue.stringValue("processDefinitionId").ifPresent(it -> {
            builder.processDefinitionId(it);
        });

        annotationValue.get("processDefinitionIdIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")){
                builder.processDefinitionIdIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionKey").ifPresent(it -> {
            builder.processDefinitionKey(it);
        });

        annotationValue.get("processDefinitionKeyIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")) {
                builder.processDefinitionKeyIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionVersionTag").ifPresent(it -> {
            builder.processDefinitionVersionTag(it);
        });

        annotationValue.booleanValue("withoutTenantId").ifPresent(it -> {
            if (it) {
                builder.withoutTenantId();
            }
        });

        annotationValue.get("tenantIdIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")) {
                builder.tenantIdIn(it);
            }
        });

        annotationValue.booleanValue("includeExtensionProperties").ifPresent(it -> {
            builder.includeExtensionProperties(it);
        });

        builder.open();

        log.info("External task client subscribed to topic {}", annotationValue.stringValue("topicName").get());

    }
}

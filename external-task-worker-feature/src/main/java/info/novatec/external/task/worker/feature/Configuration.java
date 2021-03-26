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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;

import java.util.Optional;

/**
 * @author Martin Sawilla
 */
@Context
@ConfigurationProperties("camunda.external-client")
public interface Configuration {

    /**
     * Base url of the Camunda BPM Platform REST API. This information is mandatory.
     *
     * @return the base url
     */
    Optional<String> getBaseUrl();

    /**
     * A custom worker id the Workflow Engine is aware of. This information is optional. Note: make sure to choose a
     * unique worker id If not given or null, a worker id is generated automatically which consists of the hostname as
     * well as a random and unique 128 bit string (UUID).
     *
     * @return the worker id - the Workflow Engine is aware of
     */
    Optional<String> getWorkerId();

    /**
     * Specifies the maximum amount of tasks that can be fetched within one request. This information is optional.
     * Default is 10.
     *
     * @return the amount of tasks which are supposed to be fetched within one request
     */
    Optional<Integer> getMaxTasks();

    /**
     * Specifies whether tasks should be fetched based on their priority or arbitrarily. This information is optional.
     * Default is true.
     *
     * @return if usePriority should be used
     */
    Optional<Boolean> getUsePriority();

    /**
     * Specifies the serialization format that is used to serialize objects when no specific format is requested.
     * This option defaults to application/json.
     *
     * @return the serialization format
     */
    Optional<String> getDefaultSerializationFormat();

    /**
     * Specifies the date format to de-/serialize date variables.
     *
     * @return the date format
     */
    Optional<String> getDateFormat();

    /**
     * Asynchronous response (long polling) is enabled if a timeout is given. Specifies the maximum waiting time for
     * the response of fetched and locked external tasks. The response is performed immediately, if external tasks are
     * available in the moment of the request. This information is optional. Unless a timeout is given, fetch and lock
     * responses are synchronous.
     *
     * @return the async response timeout of fetched and locked external tasks in milliseconds
     */
    Optional<Long> getAsyncResponseTimeout();

    /**
     * The lock duration in milliseconds to lock the external tasks
     * <ul>
     *     <li>must be greater than zero</li>
     *     <li>the default lock duration is 20 seconds (20,000 milliseconds)</li>
     *     <li>is overridden by the lock duration configured on a topic subscription</li>
     * </ul>
     *
     * @return the lock duration of external tasks
     */
    Optional<Long> getLockDuration();

    /**
     * Disables the client-side backoff strategy. On invocation, the configuration option backoffStrategy is ignored.
     * NOTE: Please bear in mind that disabling the client-side backoff can lead to heavy load situations on engine
     * side. To avoid this, please specify an appropriate async response timeout.
     *
     * @return if the backoff strategy should be disabled
     */
    Optional<Boolean> getDisableBackoffStrategy();
}

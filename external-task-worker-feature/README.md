# micronaut-external-task-worker
This open source project allows you to easily integrate the Camunda external worker into Micronaut Projects. It is a 
subproject of micronaut-camunda-bpm.

<details>
    <summary>Click to show Gradle configuration</summary>
TODO
</details>
<details>
    <summary>Click to show Maven configuration</summary>
TODO
</details>

# Table of Contents
TODO
## Getting started

See our simple [Example application with Java/Gradle](/../example-external-task-worker).

Go to Micronaut Launch and create either a Micronaut Application or a Command Line Application. 
The minimal configuration requires you to provide a handler for a specific topic, and a configuration that points to the 
camunda rest api. You can register multiple handlers in this way for different topics.

Example handler:
```java 
import info.novatec.external.task.worker.feature.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import javax.inject.Singleton;

@Singleton
@ExternalTaskSubscription(topicName = "your topic name")
public class ExampleHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
    // Put your business logic here    
    }
}
```
Minimal configuration in application.yml: 
```yaml
external-client:
  base-url: the url of your camunda rest api
```

On start of your application the external task worker will automatically connect to the provided camunda rest api and 
start fetching tasks.

## TopicSubscription
To subscribe to a specific topic you just need to place the ExternalTaskSubscription Annotation on your handler.

Example:
```java
@ExternalTaskSubscription(topicName = "your topic name")
public class ExampleHandler implements ExternalTaskHandler {}
```

The annotation accepts the following properties:

| Property                    | Default | Description                                                                  |
|-----------------------------|---------|------------------------------------------------------------------------------|
| topicName                   |         | Mandatory, The topic name the client subscribes to.                          |
| lockDuration                | 20000   | Lock duration in milliseconds to lock external tasks. Must be greater than zero. |
| variables                   |         | The name of the variables that are supposed to be retrieved.                 |
| localVariables              | false   | Whether or not variables from greater scope than the external task should be fetched. false means all variables visible in the scope of the external task will be fetched, true means only local variables (to the scope of the external task) will be fetched. |
| businessKey                 |         | A business key to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionId         |         | A process definition id to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionIdIn       |         | Process definition ids to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionKey        |         | A process definition key to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionKeyIn      |         | Process definition keys to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionVersionTag |         |                                                                              |
| withoutTenantId             | false   | Filter for external tasks without tenant.                                    |
| tenantIdIn                  |         | Tenant ids to filter for external tasks that are supposed to be fetched and locked. |
| includeExtensionProperties  | false   | Whether or not to include custom extension properties for fetched external tasks. true means all extensionProperties defined in the external task activity will be provided. false means custom extension properties are not available within the external-task-client |

## Configuration Properties

You may use the following properties (typically in application.yml) to configure the external task client.

| Prefix                | Property         | Default               | Description                                       |
|-----------------------|------------------|-----------------------|---------------------------------------------------|
| camunda.external-client | .base-url      |                       | Mandatory, Base url of the Camunda BPM Platform REST API. |
|                       | .worker-id       | Generated out of hostname + 128 Bit UUID | A custom worker id the Workflow Engine is aware of. |
|                       | .max-Tasks       | 10                    | Maximum amount of tasks that will be fetched with each request. |
|                       | .use-priority    | true                  | Specifies whether tasks should be fetched based on their priority or arbitrarily. |
|                       | .default-serialization-format | application/json | Specifies the serialization format that is used to serialize objects when no specific format is requested. |
|                       | .date-format     |                       | Specifies the date format to de-/serialize date variables. |
|                       | .async-response-timeout |                | Asynchronous response (long polling) is enabled if a timeout is given. Specifies the maximum waiting time for the response of fetched and locked external tasks. The response is performed immediately, if external tasks are available in the moment of the request. Unless a timeout is given, fetch and lock responses are synchronous. |
|                       | .lock-duration   | 20000 (milliseconds)  | Lock duration in milliseconds to lock external tasks. Must be greater than zero. This gets overridden by the lock duration configured on a topic subscription |
|                       | .disabled-auto-fetching |            | TODO: Currently not supported yet |
|                       | .disable-backoff-strategy | false        | Disables the client-side backoff strategy. On invocation, the configuration option backoffStrategy is ignored. Please bear in mind that disabling the client-side backoff can lead to heavy load situations on engine side. To avoid this, please specify an appropriate async-response-timeout(long). |

## Custom External Task Client

With the following bean it is possible to customize the external task client:
```java
import info.novatec.external.task.worker.feature.ExternalClientCustomizer;
import io.micronaut.context.annotation.Replaces;
import org.camunda.bpm.client.ExternalTaskClientBuilder;

import javax.inject.Singleton;

@Singleton
@Replaces(ExternalClientCustomizer.class)
public class MyExternalClientCustomizer implements ExternalClientCustomizer {

    @Override
    public void customize(ExternalTaskClientBuilder builder) {
        // Do your customization here e.g.:
        builder.asyncResponseTimeout(1000);
    }
}
```
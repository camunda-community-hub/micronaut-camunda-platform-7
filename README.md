# micronaut-camunda-bpm
**Integration between Micronaut and Camunda BPM**

This project allows you to easily integrate [Camunda BPM](https://camunda.com/products/bpmn-engine/) into existing [Micronaut](https://micronaut.io) projects.

We configure Camunda BPM with sensible defaults, so that you can get started with minimum configuration: simply add a dependency in your Micronaut project!

Advantages of Micronaut together with Camunda BPM:
* Monumental leap in startup time (Currently blocked by slow MyBatis initialization)
* Minimal memory footprint
* (...)

Advantages of Camunda BPM together with Micronaut:
* BPMN 2.0 Support
* Embedded process engine with low memory footprint
* (...)

Do you want to contribute to our open source project? Please read the [Contribution Guidelines](CONTRIBUTING.md) and [contact us](#contact).

Micronaut + Camunda BPM = :heart:

[![Release](https://img.shields.io/github/v/release/NovatecConsulting/micronaut-camunda-bpm.svg)](https://github.com/NovatecConsulting/micronaut-camunda-bpm/releases)
[![License](https://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Continuous Integration](https://github.com/NovatecConsulting/micronaut-camunda-bpm/workflows/Continuous%20Integration/badge.svg)](https://github.com/NovatecConsulting/micronaut-camunda-bpm/actions)
[![Join the chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/NovatecConsulting/micronaut-camunda-bpm)

# Features
* Camunda BPM can be integrated as an embedded process engine into a Micronaut project by simply [adding a dependency](#add-dependency-using-gradle) in build.gradle (Gradle) or pom.xml (Maven).
* Using H2 as an in-memory database is as simple as [adding a dependency](#add-dependency-using-gradle). Other [data sources can be configured](#data-source) via properties.
* Models (*.bpmn, *.cmmn, and *.dmn) found in the root of the resources are [automatically deployed](#deploying-process-models).
* The Camunda process engine with its job executor is started automatically - but disabled for tests by default.
* The process engine and related services, e.g. RuntimeService, RepositoryService, ..., are provided as lazy initialized beans and [can be injected](#calling-camunda-bpm-process-engine-and-related-services).
* Micronaut beans are resolved from the application context if they are [referenced by expressions or Java class names](#invoking-java-delegates) within the process models.
* The process engine [integrates with Micronaut's transaction manager](#using-micronaut-data-jdbc-or-micronaut-data-jpa). Optionally, micronaut-data-jdbc or micronaut-data-jpa are supported.
* The process engine can be configured with [generic properties](#generic-properties).
* The [process engine configuration](#custom-process-engine-configuration) and the [job executor configuration](#custom-jobexecutor-configuration) can be customized programmatically.
* A Camunda admin user is created if configured by [properties](#properties) and not present yet (including admin group and authorizations).
* Camunda BPM's telemetry feature is automatically deactivated during test execution.

# Getting Started

This section describes what needs to be done to use `micronaut-camunda-bpm-feature` in a Micronaut project.

We officially support the JDK LTS releases 8 and 11 and the latest release supported by Micronaut (currently JDK 15).

Do you need an example? See our example application at [/micronaut-camunda-bpm-example](/micronaut-camunda-bpm-example). 

## Add Dependency using Gradle
1. (Optionally) create an empty Micronaut project with `mn create-app my-example` or use [Micronaut Launch](https://launch.micronaut.io).
2. Add the dependency in build.gradle:
```groovy
implementation("info.novatec:micronaut-camunda-bpm-feature:0.15.0")
runtimeOnly("com.h2database:h2")
```

Note: The module `micronaut-camunda-bpm-feature` includes the dependency `org.camunda.bpm:camunda-engine` which will be resolved transitively.

## Add Dependency using Maven
1. (Optionally) create an empty Micronaut project with `mn create-app my-example --build=maven` or use [Micronaut Launch](https://launch.micronaut.io).
2. Add the dependency in pom.xml:
```xml
<dependency>
  <groupId>info.novatec</groupId>
  <artifactId>micronaut-camunda-bpm-feature</artifactId>
  <version>0.15.0</version>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

Note: The module `micronaut-camunda-bpm-feature` includes the dependency `org.camunda.bpm:camunda-engine` which will be resolved transitively.

##  Deploying process models
To deploy a process model create an executable BPMN file and save it in the resources' root. When starting the application you'll see the logs saying:

`Deploying model: helloworld.bpmn`

## Calling Camunda BPM Process Engine and related Services

Inject the process engine or any related services using constructor injection:
```java
// ...

import javax.inject.Singleton;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;

@Singleton
public class MyComponent {

    private final ProcessEngine processEngine;
    private final RuntimeService runtimeService;
    
    public MyComponent(ProcessEngine processEngine, RuntimeService runtimeService) {
        this.processEngine = processEngine;
        this.runtimeService = runtimeService;
    }

    // ...
}
```

Alternatively to constructor injection, you can also use field injection, JavaBean property injection, or method parameter injection.

## Invoking Java Delegates

To invoke a Java Delegate first create a singleton bean:

```java
import javax.inject.Singleton;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Singleton
public class LoggerDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(LoggerDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Hello World: {}", delegateExecution);
    }
}
```

and then reference it the process model with the expression`${loggerDelegate}`.

## Process Tests

Process tests can easily be implemented with JUnit 5 by adding the `camunda-bpm-assert` library as a dependency:

<details>
<summary>Click to show Gradle dependencies</summary>

```groovy
testImplementation("org.camunda.bpm.assert:camunda-bpm-assert:8.0.0")
testImplementation("org.assertj:assertj-core:3.16.1")
```
</details>

<details>
<summary>Click to show Maven dependencies</summary>

```xml
<dependency>
    <groupId>org.camunda.bpm.assert</groupId>
    <artifactId>camunda-bpm-assert</artifactId>
    <version>8.0.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.16.1</version>
    <scope>test</scope>
</dependency>
```
</details>

and then implement the test using the usual `@MicronautTest` annotation:

```java
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@MicronautTest
class HelloWorldProcessTest {

    @Inject
    RuntimeService runtimeService;

    @Test
    void happyPath() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorld");
        assertThat(processInstance).isStarted();

        assertThat(processInstance).isWaitingAt("TimerEvent_Wait");
        execute(job());

        assertThat(processInstance).isEnded();
    }
}
```

Note: the integration automatically disables the job executor and the process engine's telemetry feature during test execution. This is deduced from the "test" profile.

See also a test in our example application: [HelloWorldProcessTest](/micronaut-camunda-bpm-example/src/test/java/info/novatec/micronaut/camunda/bpm/example/HelloWorldProcessTest.java)

## Executing Blocking Operations on Netty's I/O Thread Pool 
When using the default server implementation Netty, blocking operations must be performed on I/O instead of Netty threads to avoid possible deadlocks. Therefore, as soon as Camunda ["borrows a client thread"](https://docs.camunda.org/manual/current/user-guide/process-engine/transactions-in-processes/)  you have to make sure that the [event loop is not blocked](https://objectcomputing.com/resources/publications/sett/june-2020-micronaut-2-dont-let-event-loops-own-you).
A frequently occurring example is the implementation of a REST endpoint which interacts with the process engine. By default, Micronaut would use a Netty thread for this blocking operation. To prevent the use of a Netty thread it is recommended to use the annotation [`@ExecuteOn(TaskExecutors.IO)`](https://docs.micronaut.io/latest/guide/index.html#reactiveServer). This will make sure that an I/O thread is used.

```java
@Post("/hello-world-process")
@ExecuteOn(TaskExecutors.IO)
public String startHelloWorldProcess() {
    return runtimeService.startProcessInstanceByKey("HelloWorld").getId();
}
```

## Configuration

### Data Source

By default, an in-memory H2 data source is preconfigured. Remember to add the runtime dependency `com.h2database:h2` mentioned in [Getting Started](#getting-started).

However, you can configure any other database, e.g. in `application.yml`:

```yaml
datasources:
  default:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: secret
    driver-class-name: org.postgresql.Driver
```

after adding the appropriate driver as a dependency:

```groovy
runtimeOnly "org.postgresql:postgresql:42.2.18"
```

### Connection Pool with HikariCP

This integration uses HikariCP as a database connection pool to optimize performance. By default, the following configuration is applied:
* `datasources.default.minimum-idle: 10`
* `datasources.default.maximum-pool-size: 50`

### Properties

You may use the following properties (typically in application.yml) to configure the Camunda BPM integration.

| Prefix                |Property          | Default                                      | Description            |
|-----------------------|------------------|----------------------------------------------|------------------------|
| camunda.bpm.admin-user| .id             |                                               | If present, a Camunda admin account will be created by this id (including admin group and authorizations) |
| camunda.bpm.admin-user| .password       |                                               | Admin's password (mandatory if the id is present)  |
| camunda.bpm.admin-user| .firstname      |                                               | Admin's firstname (mandatory if the id is present) |
| camunda.bpm.admin-user| .lastname       |                                               | Admin's lastname (mandatory if the id is present) |
| camunda.bpm.admin-user| .email          |                                               | Admin's email address (optional) |

## Generic Properties

The process engine can be configured using generic properties listed in Camunda's Documentation: [Configuration Properties](https://docs.camunda.org/manual/latest/reference/deployment-descriptors/tags/process-engine/#configuration-properties).

The properties can be set in kebab case (lowercase and hyphen separated) or camel case (indicating the separation of words with a single capitalized letter as written in Camunda's documentation). Kebab case is preferred when setting properties.

Some of the most relevant properties are:
* database-schema-update (databaseSchemaUpdate)
* history
* initialize-telemetry (initializeTelemetry)
* telemetry-reporter-activate (telemetryReporterActivate)

Example:

```yaml
camunda:
  bpm:
    generic-properties:
      properties:
        initialize-telemetry: true
```

### Custom Process Engine Configuration
With the following bean it's possible to customize the process engine configuration:

```java
import info.novatec.micronaut.camunda.bpm.feature.DefaultProcessEngineConfigurationCustomizer;
import info.novatec.micronaut.camunda.bpm.feature.MnProcessEngineConfiguration;
import info.novatec.micronaut.camunda.bpm.feature.ProcessEngineConfigurationCustomizer;
import io.micronaut.context.annotation.Replaces;
import javax.inject.Singleton;

@Singleton
@Replaces(DefaultProcessEngineConfigurationCustomizer.class)
public class MyProcessEngineConfigurationCustomizer implements ProcessEngineConfigurationCustomizer {
    @Override
    public void customize(MnProcessEngineConfiguration processEngineConfiguration) {
        // configure process engine configuration here, e.g.:
        processEngineConfiguration.setProcessEngineName("CustomizedEngine");
    }
}
```

### Custom JobExecutor Configuration
With the following bean it's possible to customize the job executor:

```java
import info.novatec.micronaut.camunda.bpm.feature.DefaultJobExecutorCustomizer;
import info.novatec.micronaut.camunda.bpm.feature.JobExecutorCustomizer;
import info.novatec.micronaut.camunda.bpm.feature.MnJobExecutor;
import io.micronaut.context.annotation.Replaces;
import javax.inject.Singleton;

@Singleton
@Replaces(DefaultJobExecutorCustomizer.class)
public class MyJobExecutorCustomizer implements JobExecutorCustomizer {
    @Override
    public void customize(MnJobExecutor jobExecutor) {
        jobExecutor.setWaitTimeInMillis(300);
    }
}
```

## Using micronaut-data-jdbc or micronaut-data-jpa

The process engine integrates with Micronaut's transaction manager and uses Hikari Connection Pool:
* When interacting with the process engine, e.g. starting or continuing a process, the existing transaction will be propagated.
* JavaDelegates and Listeners will have the surrounding Camunda transaction propagated to them allowing the atomic persistence of data.

Optionally, micronaut-data-jdbc or micronaut-data-jpa are supported.

### Alternative 1: micronaut-data-jdbc

To enable embedded transactions management support **with micronaut-data-jdbc** please add the following dependencies to your project:

<details>
<summary>Click to show Gradle dependencies</summary>

```groovy
annotationProcessor("io.micronaut.data:micronaut-data-processor")
implementation("io.micronaut.data:micronaut-data-jdbc")
```
</details>

<details>
<summary>Click to show Maven dependencies</summary>

```xml
<dependency>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-jdbc</artifactId>
</dependency>
```

And also add the annotation processor to every (!) `annotationProcessorPaths` element:

```xml
<path>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-processor</artifactId>
  <version>${micronaut.data.version}</version>
</path>
```
</details>

and then configure the JDBC properties as described [micronaut-sql documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/#jdbc).

### Alternative 2: micronaut-data-jpa

To enable embedded transactions management support **with micronaut-data-jpa** please add the following dependencies to your project:

<details>
<summary>Click to show Gradle dependencies</summary>

```groovy
annotationProcessor("io.micronaut.data:micronaut-data-processor")
implementation("io.micronaut.data:micronaut-hibernate-jpa")
```
</details>

<details>
<summary>Click to show Maven dependencies</summary>

```xml
<dependency>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-hibernate-jpa</artifactId>
</dependency>
```

And also add the annotation processor to every (!) `annotationProcessorPaths` element:

```xml
<path>
  <groupId>io.micronaut.data</groupId>
  <artifactId>micronaut-data-processor</artifactId>
  <version>${micronaut.data.version}</version>
</path>
```
</details>

and then configure JPA as described in [micronaut-sql documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/#hibernate).

## Compatibility Matrix

The following compatibility matrix shows the officially supported Micronaut and Camunda BPM versions for each release.
Other combinations might also work but have not been tested.  

| Release |Micronaut | Camunda BPM |
|--------|-------|--------|
| 0.15.0 | 2.3.0 | 7.14.0 |
| 0.14.0 | 2.2.3 | 7.14.0 |
| 0.13.0 | 2.2.2 | 7.14.0 |
| 0.12.0 | 2.2.1 | 7.14.0 |
| 0.11.0 | 2.2.1 | 7.14.0 |
| 0.10.1 | 2.2.0 | 7.14.0 |
| 0.10.0 | 2.2.0 | 7.14.0 |
| 0.9.0 | 2.1.3 | 7.14.0 |
| 0.8.0 | 2.1.2 | 7.13.0 |
| 0.7.0 | 2.1.1 | 7.13.0 |
| 0.6.0 | 2.1.0 | 7.13.0 |
| 0.5.3 | 2.0.1 | 7.13.0 |
| 0.5.2 | 2.0.0 | 7.13.0 |
| 0.5.1 | 2.0.0 | 7.13.0 |
| 0.5.0 | 2.0.0 | 7.13.0 |
| 0.4.2 | 1.3.6 | 7.13.0 |
| 0.3.1 | 1.3.5 | 7.12.0 |
| 0.2.2 | 1.3.3 | 7.12.0 |
| 0.2.1 | 1.3.3 | 7.12.0 |
| 0.2.0 | 1.3.3 | 7.12.0 |
| 0.1.0 | 1.3.3 | 7.12.0 |

Download of Releases:
* [GitHub Artifacts](https://github.com/NovatecConsulting/micronaut-camunda-bpm/releases)
* [Maven Central Artifacts](https://search.maven.org/artifact/info.novatec/micronaut-camunda-bpm-feature)

## Contact

This open source project is being developed by [Novatec Consulting GmbH](https://www.novatec-gmbh.de/en/) with the support of the open source community.

If you have any questions or ideas feel free to create an [issue](https://github.com/NovatecConsulting/micronaut-camunda-bpm/issues) or contact us via Gitter or mail.

We'd also like to hear from you if you're using the project :-)

Do you want to contact the core team?
* [Chat via Gitter](https://gitter.im/NovatecConsulting/micronaut-camunda-bpm) 
* [mailto:micronaut-camunda@novatec-gmbh.de](mailto:micronaut-camunda@novatec-gmbh.de)

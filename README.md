# micronaut-camunda-bpm
**Integration between Micronaut and Camunda BPM Process Engine**

This project allows you to easily integrate the [Camunda BPM Process Engine](https://camunda.com/products/bpmn-engine/) into existing [Micronaut](https://micronaut.io) projects.

We configure Camunda BPM with sensible defaults, so that you can get started with minimum configuration: simply add a dependency in your Micronaut project!

Advantages of Micronaut together with Camunda BPM:
* Monumental leap in startup time (Currently still blocked by [micronaut-core#2867](https://github.com/micronaut-projects/micronaut-core/issues/2867))
* Minimal memory footprint
* (...)

Advantages of Camunda BPM together with Micronaut:
* BPMN 2.0 Support
* Embedded process engine with low memory footprint
* (...)

Do you want to contribute to our open source project? Please read the [Contribution Guidelines](CONTRIBUTING.md).

Do you want to contact the core team directly? Please send us a mail to [micronaut-camunda@novatec-gmbh.de](mailto:micronaut-camunda@novatec-gmbh.de).

Micronaut + Camunda BPM = :heart:

[![Release](https://img.shields.io/github/v/release/NovatecConsulting/micronaut-camunda-bpm.svg)](https://github.com/NovatecConsulting/micronaut-camunda-bpm/releases)
[![License](https://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Continuous Integration](https://github.com/NovatecConsulting/micronaut-camunda-bpm/workflows/Continuous%20Integration/badge.svg)](https://github.com/NovatecConsulting/micronaut-camunda-bpm/actions)

# Features
* Camunda BPM can be integrated into a Micronaut project by simply adding a dependency in build.gradle (Gradle) or pom.xml (Maven).
* Using h2 as an in-memory database is as simple as adding a dependency. Other data sources can be configured via properties.
* The Camunda process engine with its job executor is started automatically.
* Models (*.bpmn, *.cmmn, and *.dmn) found in the root of the resources are automatically deployed.
* The process engine and related services, e.g. RuntimeService, RepositoryService, ..., are provided as lazy initialized beans and can be injected.
* Micronaut beans are resolved from the application context if they are referenced in expressions within the process models.

# Getting Started

This section describes what needs to be done to use `micronaut-camunda-bpm-feature` in a Micronaut project.

Do you need an example? See our example application at [/micronaut-camunda-bpm-example](/micronaut-camunda-bpm-example). 

## Add Dependency using Gradle
1. (Optionally) create an empty Micronaut project with `mn create-app my-example` or use [Micronaut Launch](https://launch.micronaut.io).
2. Add the dependency in build.gradle:
```groovy
implementation "info.novatec:micronaut-camunda-bpm-feature:0.5.1"
implementation "com.h2database:h2"
implementation "org.camunda.bpm:camunda-engine:7.13.0"
```

## Add Dependency using Maven
1. (Optionally) create an empty Micronaut project with `mn create-app my-example --build=maven` or use [Micronaut Launch](https://launch.micronaut.io).
2. Add the dependency in pom.xml:
```xml
<dependency>
  <groupId>info.novatec</groupId>
  <artifactId>micronaut-camunda-bpm-feature</artifactId>
  <version>0.5.1</version>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
<dependency>
  <groupId>org.camunda.bpm</groupId>
  <artifactId>camunda-engine</artifactId>
  <version>7.13.0</version>
</dependency>
```

##  Deploying process models
To deploy a process model create an executable BPMN file and save it in the resources' root. When starting the application you'll see the logs saying:

`Deploying model: helloworld.bpmn`

## Calling Camunda BPM Process Engine and related Services

Inject the process engine or any related services using constructor injection:
```java
// ...

import javax.inject.Singleton;

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

## Configuration

You may use the following properties (typically in application.yml) to configure the Camunda BPM integration.

| Prefix               |Property          | Default                                      | Description            |
|----------------------|------------------|----------------------------------------------|------------------------|
| datasources.default  | .url             | jdbc:h2:mem:micronaut-db;DB_CLOSE_DELAY=1000 | Database URL           |
|                      | .username        | sa                                           | User name for database |
|                      | .password        |                                              | Password for database  |
|                      | .driver-class-name | org.h2.Driver                              | Driver for database    |
| camunda.bpm          | .history-level   | auto                                         | Camunda history level, use one of [`full`, `audit`, `activity`, `none`, `auto`]. `auto` uses the level already present in the database, defaulting to `full`. |
| camunda.bpm.database | .schema-update   | true                                         | If automatic schema update should be applied, use one of [`true`, `false`, `create`, `create-drop`, `drop-create`] |

### Custom Process Engine Configuration

Internally, to build Camunda `ProcessEngine` we use `ProcessEngineConfiguration`. This process can be intercepted for detailed configuration customization with the following bean:

```java
@Singleton
@Replaces(DefaultProcessEngineConfigurationCustomizer.class)
public class MyProcessEngineConfigurationCustomizer implements ProcessEngineConfigurationCustomizer  {

    @Override
    public void customize(ProcessEngineConfiguration configuration) {
        // configure ProcessEngineConfiguration here, e.g.:
        configuration.setProcessEngineName("CustomizedEngine");
    }

}
```    

## Compatibility Matrix

The following compatibility matrix shows the officially supported Micronaut and Camunda BPM versions for each release.
Other combinations will probably also work but have not been tested.  

| Release |Micronaut | Camunda BPM |
|-------|-------|--------|
| 0.5.1 | 2.0.0 | 7.13.0 |
| 0.5.0 | 2.0.0 | 7.13.0 |
| 0.4.2 | 1.3.6 | 7.13.0 |
| 0.3.1 | 1.3.5 | 7.12.0 |
| 0.2.2 | 1.3.3 | 7.12.0 |
| 0.2.1 | 1.3.3 | 7.12.0 |
| 0.2.0 | 1.3.3 | 7.12.0 |
| 0.1.0 | 1.3.3 | 7.12.0 |

## Contact

This open source project is being developed by [Novatec Consulting GmbH](https://www.novatec-gmbh.de/en/) with the support of the open source community.

If you have any questions or ideas feel free to create an [issue](https://github.com/NovatecConsulting/micronaut-camunda-bpm/issues) or contact us via mail.

We'd also like to hear from you if you're using the project :-)

You can contact the main developers via mail at [micronaut-camunda@novatec-gmbh.de](mailto:micronaut-camunda@novatec-gmbh.de).

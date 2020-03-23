# micronaut-camunda-bpm
**Integration between Micronaut and Camunda BPM Process Engine**

This project allows you to easily integrate the [Camunda BPM Process Engine](https://camunda.com/products/bpmn-engine/) into existing [Micronaut](https://micronaut.io) projects.

We take an opinionated view of Camunda BPM, so that you can get started with minimum configuration.

Advantages of Micronaut together with Camunda:
* Monumental leap in startup time (Currently still blocked by [micronaut-core#2867](https://github.com/micronaut-projects/micronaut-core/issues/2867))
* Minimal memory footprint
* (...)

Advantages of Camunda BPM together with Micronaut:
* BPMN 2.0 Support
* Embedded process engine with low memory footprint
* (...)

Micronaut + Camunda = :heart:

[![Release](https://img.shields.io/github/v/release/NovatecConsulting/micronaut-camunda-bpm.svg)](https://github.com/NovatecConsulting/micronaut-camunda-bpm/releases)
[![License](https://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Continuous Integration](https://github.com/NovatecConsulting/micronaut-camunda-bpm/workflows/Continuous%20Integration/badge.svg)](https://github.com/NovatecConsulting/micronaut-camunda-bpm/actions)

## Features
* Camunda BPM can be integrated into a Micronaut project by simply adding a dependency in build.gradle (Gradle) or pom.xml (Maven).
* Using h2 as an in-memory database is as simple as adding a dependency. Other data sources can be configured via properties.
* Camunda Process engine with job executor is started automatically.
* Models (*.bpmn, *.cmmn, and *.dmn) found in the classpath are automatically deployed.
* Micronaut beans are resolved from the application context if they are referenced in expressions within the process models.

Please also read the [FAQ](FAQ.md).

# User Guide

## Using Gradle
1. (Optionally) create an empty Micronaut project with `mn create-app my-example`
2. Add the dependency in build.gradle:
```
implementation "info.novatec:micronaut-camunda-bpm-feature:0.1.0"
implementation "com.h2database:h2"
```

## Using Maven
1. (Optionally) create an empty Micronaut project with `mn create-app my-example --build=maven`
2. Add the dependency in pom.xml:
```
<dependency>
  <groupId>info.novatec</groupId>
  <artifactId>micronaut-camunda-bpm-feature</artifactId>
  <version>0.1.0</version>
</dependency>
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
```

## Configuration

You may use the following properties (typically in application.yml) to configure the Camunda BPM integration.

| Prefix               |Property          | Default                                      | Description            |
|----------------------|------------------|----------------------------------------------|------------------------|
| datasources.default  | .url             | jdbc:h2:mem:micronaut-db;DB_CLOSE_DELAY=1000 | Database URL           |
|                      | .username        | sa                                           | User name for database |
|                      | .password        |                                              | Password for database  |
|                      | .driverClassName | org.h2.Driver                                | Driver for database    |
| camunda.bpm.database | .schema-update   | true                                         | If automatic schema update should be applied, use one of [true, false, create, create-drop, drop-create] |

# Developer Guide
## Get the code

Create a local Git clone:

`git clone https://github.com/NovatecConsulting/micronaut-camunda-bpm.git`

## Open and run in IntelliJ IDEA

To import the project into IntelliJ IDEA simply open the build.gradle file and follow the instructions to import the project.

For IntelliJ IDEA if you plan to use the IntelliJ compiler then you should enable annotation processing under the "Build, Execution, Deployment → Compiler → Annotation Processors" by ticking the "Enable annotation processing" checkbox.

Once you have enabled annotation processing in IntelliJ you can run the application and tests directly within the IDE without the need of an external build tool such as Gradle.

## Build and run the example application

Unix:
```
./gradlew clean run
```

Windows:

```
gradlew.bat clean run
```

## Call the example app

Open in your browser:
* http://localhost:8080/camunda/name will return "default" as the name of the default process engine.
* http://localhost:8080/camunda/definitions will return "HelloWorld" as the currently deployed process model.

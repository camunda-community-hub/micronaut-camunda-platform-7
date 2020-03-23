# micronaut-camunda-bpm
**Integration between Micronaut and Camunda BPM Process Engine**

This project allows you to easily integrate the [Camunda BPM Process Engine](https://camunda.com/products/bpmn-engine/) into existing [Micronaut](https://micronaut.io) projects.

Advantages of Micronaut together with Camunda:
* Monumental leap in startup time
* Minimal memory footprint
* (...)

Advantages of Camunda BPM together with Micronaut:
* BPMN 2.0 Support
* Embedded process engine with low memory footprint
* (...)

Micronaut + Camunda = :heart:

> **NOTE:**
> Currently, this project is a proof of concept. In future it will hopefully be as easy as adding a dependency to your Micronaut project and optionally overwriting some default configuration values to have an embedded Camunda BPM engine up and running.

## Features
* Camunda BPM can be integrated into a Micronaut project by simply adding a dependency in build.gradle (Gradle) or pom.xml (Maven). (not yet available on Maven Central)
* Camunda Process engine is started automatically with H2 in-memory database by adding H2 as a dependency.
* Models (*.bpmn, *.cmmn, and *.dmn) found in the classpath are automatically deployed.
* Micronaut beans are resolved from the application context if they are referenced in expressions within the process models.

Please also read the [FAQ](FAQ.md).

[![Continuous Integration](https://github.com/NovatecConsulting/micronaut-camunda-bpm/workflows/Continuous%20Integration/badge.svg)](https://github.com/NovatecConsulting/micronaut-camunda-bpm/actions)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# User Guide

In build.gradle add the dependency:
```
implementation "info.novatec:micronaut-camunda-bpm-feature:0.0.1-SNAPSHOT"
```


## Configuration

You may use the following properties (typically in in application.yml) to configure the Camunda BPM integration.

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

## Build and run in console

Unix:
```
./gradlew clean run
```

Windows:

```
gradlew.bat clean run
```

## Run the Hello World app

Open http://localhost:8080/greet/YourName in your browser

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
* Models (*.bpmn, *.cmmn, and *.dmn) found in the classpath are automatically deployed. (currently only resources in root directory)
* Micronaut beans are resolved from the application context if they are referenced in expressions within the process models.

Please also read the [FAQ](FAQ.md).

[![Build Status](https://api.travis-ci.org/NovatecConsulting/micronaut-camunda-bpm.svg?branch=master)](https://travis-ci.org/NovatecConsulting/micronaut-camunda-bpm)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# Getting Started
## Install Micronaut CLI
Install SDKMAN and Micronaut CLI by following the instructions on https://micronaut.io/download.html

To later update the `mn` CLI application invoke `sdk install micronaut` again.

## Run in IntelliJ IDEA

To import a Micronaut project into IntelliJ IDEA simply open the build.gradle file and follow the instructions to import the project.

For IntelliJ IDEA if you plan to use the IntelliJ compiler then you should enable annotation processing under the "Build, Execution, Deployment → Compiler → Annotation Processors" by ticking the "Enable annotation processing" checkbox.

Once you have enabled annotation processing in IntelliJ you can run the application and tests directly within the IDE without the need of an external build tool such as Gradle.

## Run in console

Unix:
```
./gradlew clean run
```

Windows:

```
gradlew.bat clean run
```

## Run the Hello World app

Open http://localhost:8080/greet/YourName in Browser
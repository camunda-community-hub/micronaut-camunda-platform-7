# micronaut-camunda-bpm
**Integration between Micronaut and Camunda BPM Process Engine**

This project allows you to easily integrate the [Camunda BPM Process Engine](https://camunda.com/products/bpmn-engine/) into exising [Micronaut](https://micronaut.io) projects.

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

# Getting Started
## Install Micronaut CLI
Install SDKMAN and Micronaut CLI by following the instructions on https://micronaut.io/download.html

To later update the `mn` CLI application invoke `sdk update micronaut`.

## Run in IntelliJ IDEA

To import a Micronaut project into IntelliJ IDEA simply open the build.gradle file and follow the instructions to import the project.

For IntelliJ IDEA if you plan to use the IntelliJ compiler then you should enable annotation processing under the "Build, Execution, Deployment → Compiler → Annotation Processors" by ticking the "Enable annotation processing" checkbox.

Once you have enabled annotation processing in IntelliJ you can run the application and tests directly within the IDE without the need of an external build tool such as Gradle.

## Run in console

Unix:
```
cd micronaut-camunda-app
./gradlew clean run
```

Windows:

```
cd micronaut-camunda-app
gradlew.bat clean run
```

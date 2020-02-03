# micronaut-camunda-bpm
Integration between Micronaut and Camunda BPM Process Engine

# Getting Started
## Install Micronaut CLI
Install SDKMAN and Micronaut CLI by following the instructions on https://micronaut.io/download.html

## Run in IntelliJ IDEA
If you are using Java or Kotlin and IntelliJ IDEA make sure you have enabled annotation processing: `Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable Annotation Processing`.

Right click on class Application and run application

## Run in console

Unix:
```
cd micronaut-camunda-app
./mvn clean install && java -jar target/micronaut-camunda-app-0.1.jar
```

Windows:

```
cd micronaut-camunda-app
mvnw clean install && java -jar target/micronaut-camunda-app-0.1.jar
```

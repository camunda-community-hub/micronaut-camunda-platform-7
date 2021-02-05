# Example Application
## Get the Code

Create a local Git clone:

`git clone https://github.com/NovatecConsulting/micronaut-camunda-bpm.git`

## Open in IntelliJ IDEA

To import the project into IntelliJ IDEA simply open the `micronaut-camunda-bpm-example/build.gradle` file and follow the instructions to import the project.

For IntelliJ IDEA if you plan to use the IntelliJ compiler then you should enable annotation processing under the "Build, Execution, Deployment → Compiler → Annotation Processors" by ticking the "Enable annotation processing" checkbox.

## Run the Example Application

### IntelliJ IDEA
To run the example application simply start `info.novatec.micronaut.camunda.bpm.example.Application` from IntelliJ IDEA.

### Unix/Mac Console
To run the application in a console on Unix/Mac:
```
./gradlew clean run -p micronaut-camunda-bpm-example
```

### Windows Command Line
To run the application in a Windows command line:
```
gradlew.bat clean run -p micronaut-camunda-bpm-example
```

## Using the Example Application

A simple process is deployed automatically.

The Camunda Cockpit is available at `http://localhost:8080/`. Login with `admin`/`admin`.

The Camunda REST API is available at the context path `/engine-rest`, e.g. `GET http://localhost:8080/engine-rest/engine`.

The following HTTP endpoints have been implemented as examples:
* `GET http://localhost:8080/example/name` will return "default" as the name of the default process engine.
* `GET http://localhost:8080/example/definitions` will return "HelloWorld" as the currently deployed process model.

## Persistent Database

By default, the example app will use an H2 in-memory database which is created on application start-up. If you need a
persistent database then the easiest approach is to configure the H2 database to be backed up by a file by configuring
the data source's URL:

`datasources.default.url: jdbc:h2:file:~/micronautdb;DB_CLOSE_ON_EXIT=FALSE`

To reset the database simply delete the `micronautdb*` files in your home directory.

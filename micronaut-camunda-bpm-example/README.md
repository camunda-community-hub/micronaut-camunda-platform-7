# Example Application
## Get the Code

Create a local Git clone:

`git clone https://github.com/camunda-community-hub/micronaut-camunda-platform-7.git`

## Open in IntelliJ IDEA

To import the project into IntelliJ IDEA simply open the top-level `build.gradle` file and follow the instructions to import the project.

If you use the IntelliJ compiler then you should enable annotation processing under the "Build, Execution, Deployment → Compiler → Annotation Processors" by ticking the "Enable annotation processing" checkbox.

## Run the Example Application

### IntelliJ IDEA
To run the application in IntelliJ IDEA simply expand the sub-module `micronaut-camunda-bpm-example` and start `info.novatec.micronaut.camunda.bpm.example.Application`.

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

### Docker
To run the application in Docker:
```
./gradlew clean dockerBuild -p micronaut-camunda-bpm-example
docker run -p 8080:8080 micronaut-camunda-bpm-example:latest
```

## Using the Example Application

Two simple processes are deployed automatically.

The Camunda Cockpit is available at `http://localhost:8080/`. Login with `admin`/`admin`.

The Camunda REST API is available at the context path `/engine-rest`, e.g. `GET http://localhost:8080/engine-rest/engine`.

The following HTTP endpoints have been implemented as examples:
* `GET http://localhost:8080/example/name` will return "default" as the name of the default process engine.
* `GET http://localhost:8080/example/definitions` will return "HelloWorld,Onboarding" as the currently deployed process models.
* `POST http://localhost:8080/example/onboarding/cancel/OnStartup` can be called to cancel an Onboarding instance by the business key "OnStartup". Further calls will fail (unless a new process instance is created manually via the Tasklist with the menu item "Start process").

## Persistent Database

By default, the example app will use an H2 in-memory database which is created on application start-up. If you need a
persistent database then the easiest approach is to configure the H2 database to be backed up by a file by configuring
the data source's URL:

`datasources.default.url: jdbc:h2:file:~/micronautdb;DB_CLOSE_ON_EXIT=FALSE`

To reset the database simply delete the `micronautdb*` files in your home directory.

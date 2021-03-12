# example-external-task-worker

This is an example application for processing Camunda external tasks with Micronaut.

This example works together with [micronaut-camunda-bpm-example](../micronaut-camunda-bpm-example/README.md)
but it may also be used in with a Camunda engine running in Spring Boot or any other
technology stack.

## Function

An item is approved if its value is less or equal than 500.

## Start the external task worker

Start external task worker application from the root directory with

`./gradlew clean run -p example-external-task-worker`

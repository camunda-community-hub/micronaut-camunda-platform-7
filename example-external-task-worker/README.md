# example-external-task-worker

Example application for processing Camunda external tasks with Micronaut.
In this example it is used in conjunction with a Camunda engine
running in micronaut-camunda-integration, but it may also be used in 
conjunction with a Camunda engine running in Spring Boot or any other
technology stack.

see also [README](../example-external-task-process/README.md) for 
example-external-task-process

## Function

An item is approved if its value is less or equal than 500.

## Start the external task worker

Start external task worker application with

`../gradlew run`
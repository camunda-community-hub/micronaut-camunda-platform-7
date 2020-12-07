# example-external-task-process

Example application for running the Micronaut-Camunda-Integration with 
external tasks. Use in conjunction with example-external-task-worker.

example-external-task-process starts a process creating an external task
every 5 seconds.

see also [README](../example-external-task-worker/README.md) for example-external-task-worker

## Start the application

Start process application with

`../gradlew run`

## REST Service for External Task Worker

The necessary REST services needed for the external task worker are exposed 
manually in MyExternalTaskRestService as the Camunda REST interface isn't
exposed by the micronaut-camunda-bpm-feature now.

# Transactional test with data-jdbc

Additional tests using Micronaut-managed transaction manager and datasource with the following additional dependencies:

```
testAnnotationProcessor("io.micronaut.data:micronaut-data-processor")
testImplementation("io.micronaut.data:micronaut-data-jdbc")
testRuntimeOnly("io.micronaut.sql:micronaut-jdbc-hikari")
```

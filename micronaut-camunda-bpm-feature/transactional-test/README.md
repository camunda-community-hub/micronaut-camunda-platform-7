# Transactional test

Additional tests using Micronaut-managed transaction manager and datasource with the following additional dependencies:

```
testImplementation("io.micronaut.data:micronaut-data-tx")
testRuntimeOnly("io.micronaut.sql:micronaut-jdbc-hikari")
```

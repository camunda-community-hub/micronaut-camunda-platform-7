FROM openjdk:14-alpine
COPY target/micronaut-camunda-example-*.jar micronaut-camunda-example.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "micronaut-camunda-example.jar"]

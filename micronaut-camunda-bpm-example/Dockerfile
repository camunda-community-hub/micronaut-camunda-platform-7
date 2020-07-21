FROM openjdk:14-alpine
COPY build/libs/micronaut-camunda-bpm-example-*-all.jar app.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "app.jar"]

FROM oracle/graalvm-ce:20.0.0 as builder
RUN gu install native-image
COPY build/libs/micronaut-camunda-app-0.1-SNAPSHOT-all.jar /home/app/micronaut/build/app.jar
COPY src/main/resources/reflectionconfig.json /home/app/micronaut
WORKDIR /home/app/micronaut
RUN native-image -jar build/app.jar
RUN ./micronaut

##Not working
##FROM frolvlad/alpine-glibc
##RUN apk add lib32stdc++6
#EXPOSE 9080
#COPY --from=builder /home/app/micronaut /app/micronaut
#ENTRYPOINT ["/app/micronaut/micronaut", "-Xmx512m"]

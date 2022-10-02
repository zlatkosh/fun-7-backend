FROM gradle:7.5 AS gradle
USER root
WORKDIR /tmp
COPY . /tmp/
RUN chmod -R 777 /tmp && gradle clean bootJar

FROM openjdk:17-jdk-alpine
RUN addgroup -S eweksd && adduser -S eweksd -G eweksd
USER eweksd
WORKDIR /app
COPY --from=gradle /tmp/build/libs/customer-support-boot*.jar customer-support.jar

ENTRYPOINT ["java","-jar","customer-support.jar"]

FROM maven:3.8.6 AS maven
USER root
WORKDIR /tmp
COPY . /tmp/
RUN chmod -R 777 /tmp && mvn clean package

FROM openjdk:17-jdk-alpine
RUN addgroup -S eweksd && adduser -S eweksd -G eweksd
USER eweksd
WORKDIR /app
COPY --from=maven /tmp/target/ads-*.jar ads.jar

ENTRYPOINT ["java","-jar","ads.jar"]

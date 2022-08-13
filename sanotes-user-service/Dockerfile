# Building image
FROM openjdk:17-alpine
VOLUME /tmp
RUN addgroup -S sanotes && adduser -S sanotes -G sanotes
USER sanotes:sanotes
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.jar"]
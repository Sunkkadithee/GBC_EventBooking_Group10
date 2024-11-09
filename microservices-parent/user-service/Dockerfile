#------------
# Build Stage
#------------
# Build Stage
FROM gradle:8-jdk22-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build -x test
#RUN gradle build -x test # -x says except from the test files since we do not need them in this case.

# -----------
# PACKAGE STAGE
# -----------
FROM openjdk:23-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/user-service.jar

# Environment variables for PostgreSQL
ENV SPRING_DATASOURCE_USERNAME=admin \
    SPRING_DATASOURCE_PASSWORD=password

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "/app/room-service.jar"]


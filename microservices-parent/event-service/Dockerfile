#----------------
#  Build Stage
#----------------
FROM gradle:8-jdk23-alpine AS builder

# Copy the project files to the Gradle user's home directory
COPY --chown=gradle:gradle . /home/gradle/src

# Set the working directory
WORKDIR /home/gradle/src

# Build the project, skipping tests
RUN gradle build -x test

#------------------
#   Package STAGE
#------------------
FROM openjdk:23-jdk

# Create the application directory
RUN mkdir /app

# Copy the built JAR file from the builder stage
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/event-service.jar

# Set environment variables for MongoDB and PostgreSQL credentials
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://event-db:5432/event-db \
    SPRING_DATASOURCE_USERNAME=admin \
    SPRING_DATASOURCE_PASSWORD=password \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect \
    MONGO_DB_USERNAME=admin \
    MONGO_DB_PASSWORD=password

# Expose the application port
EXPOSE 8089

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/event-service.jar"]

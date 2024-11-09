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
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/booking-service.jar

# Set environment variables
ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PASSWORD=password

# Expose the application port
EXPOSE 8084

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/booking-service.jar"]

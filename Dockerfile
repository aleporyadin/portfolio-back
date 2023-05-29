# Use an official OpenJDK 17 runtime as the base image
FROM openjdk:17-jdk-alpine

# Set the maintainer information
LABEL maintainer="Oleksandr Poriadin <ale.poriadin@gmail.com>"

# Set the working directory inside the container
WORKDIR /app/portfolio-back

# Copy the Gradle build files to the container
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle

# Copy the source code to the container
COPY src src

# Run the Gradle build inside the container to download dependencies and build the project
RUN ./gradlew build

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Set the command to run the application when the container starts
CMD ["java", "-jar", "build/libs/portfolio-back-0.0.1-SNAPSHOT.jar"]

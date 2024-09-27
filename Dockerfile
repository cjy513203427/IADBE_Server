# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy Gradle Wrapper scripts and necessary build files to the container
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Install dos2unix
RUN apt-get update && apt-get install -y dos2unix

# Convert line endings to Unix format and grant execute permissions to the Gradle Wrapper script
RUN dos2unix gradlew && chmod +x gradlew

# Copy the entire project source code to the container
COPY src ./src

# Use Gradle Wrapper to build the project
RUN ./gradlew build --no-daemon

# Expose the default application port
EXPOSE 8080

# Run the generated executable JAR file
CMD ["java", "-jar", "build/libs/IADBE_Server-0.0.1-SNAPSHOT.jar"]

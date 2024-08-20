# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy Gradle Wrapper scripts and necessary build files to the container
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Grant execute permissions to the Gradle Wrapper script
RUN chmod +x gradlew

# Copy the entire project source code to the container
COPY src ./src

# Use Gradle Wrapper to build the project
RUN ./gradlew build --no-daemon

# Debug step: List contents of build/libs
RUN ls -l build/libs

# Expose the default application port
EXPOSE 8080

# Set the working directory again before running the JAR
WORKDIR /app/build/libs

# Run the generated executable JAR file
CMD ["java", "-jar", "IADBE_Server-0.0.1-SNAPSHOT.jar"]

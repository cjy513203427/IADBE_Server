# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pre-built JAR file into the container
COPY jar/IADBE_Server-0.0.1-SNAPSHOT.jar /app/IADBE_Server-0.0.1-SNAPSHOT.jar

# Set file permissions (read, write, execute for all)
RUN chmod 777 /app/IADBE_Server-0.0.1-SNAPSHOT.jar

# Expose the default application port
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app/IADBE_Server-0.0.1-SNAPSHOT.jar"]

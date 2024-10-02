# Stage 1: Build the application using Maven
FROM maven:3.9.8-amazoncorretto-17-al2023 AS build
WORKDIR /app

# Copy only the necessary files to leverage Docker cache
COPY pom.xml .
COPY src ./src

# Download dependencies (leverages Docker cache)
RUN mvn dependency:go-offline

# Package the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Stage 2: Run the application using OpenJDK
FROM amazoncorretto:17
WORKDIR /app

# Install necessary packages
RUN yum update -y && yum install -y shadow-utils

# Create a non-root user for security
RUN adduser -D appuser

USER appuser

# Copy the specific JAR file from the build stage
COPY --from=build /app/target/bff-0.0.1-SNAPSHOT-exec.jar app.jar

# Expose the application's port
EXPOSE 8080

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Optional: Health check to monitor application health
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

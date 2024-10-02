# Stage 1: Build the application using Maven
FROM maven:3.9.8-amazoncorretto-17-al2023 AS build
WORKDIR /app

# Copy the parent POM first to leverage Docker cache
COPY pom.xml .

# Copy the bff microservice's POM and source code
COPY bff/pom.xml bff/pom.xml
COPY bff/src bff/src

# Download dependencies (leverages Docker cache)
RUN mvn dependency:go-offline

# Package the bff microservice (skip tests for faster builds)
RUN mvn clean package -DskipTests -pl bff -am

# Stage 2: Run the application using OpenJDK
FROM amazoncorretto:17
WORKDIR /app

# Install necessary packages using dnf
RUN dnf update -y && dnf install -y shadow-utils

# Create a non-root user for security
RUN adduser -D appuser

USER appuser

# Copy the bff JAR file from the build stage
COPY --from=build /app/bff/target/bff-0.0.1-SNAPSHOT-exec.jar app.jar

# Expose the application's port
EXPOSE 8080

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Optional: Health check to monitor application health
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

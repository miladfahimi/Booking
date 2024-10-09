# Stage 1: Build the application using Maven
FROM maven:3.9.8-amazoncorretto-17-al2023 AS build
WORKDIR /app

# Install openssh-client for GitLab CI SSH authentication
RUN dnf update -y && \
    dnf install -y shadow-utils openssh-clients

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Download dependencies (leverages Docker cache)
RUN mvn dependency:go-offline

# Package the application (skip tests for faster builds)
RUN mvn clean package -DskipTests -Pdocker

# Stage 2: Run the application using OpenJDK
FROM amazoncorretto:17-al2023
WORKDIR /app

# Install necessary packages using dnf
RUN dnf update -y && \
    dnf install -y shadow-utils

# Create a non-root user for security
RUN adduser -m appuser

USER appuser

# Copy the specific JAR file from the build stage
COPY --from=build /app/target/reservation-0.0.1-SNAPSHOT-exec.jar app.jar

# Expose the application's port
EXPOSE 8080

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# Optional: Health check to monitor application health
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Use a lightweight Java runtime
FROM amazoncorretto:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the specific JAR file into the container
COPY ./target /app

RUN echo "Contents of /app:" && ls -l /app

# Ensure the file has proper permissions
RUN chmod 755 /app/fakeSMTP-2.1-SNAPSHOT.jar


# Expose the SMTP port
EXPOSE 8025

# Define the command to run the JAR file
CMD ["java", "-jar", "/app/fakeSMTP-2.1-SNAPSHOT.jar", "-p", "8025", "-s", "-b"]

# Use the official Gradle image to build the application
FROM gradle:7.6.2-jdk17-alpine AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Use the official OpenJDK image to run the application
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/backend/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3.9.8-amazoncorretto-8-al2023 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

ARG JAR_FILE=target/restaurants-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_restaurants.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app_restaurants.jar"]
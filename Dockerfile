FROM openjdk:17-jdk-slim
WORKDIR /app
COPY backend/target/backend-3.3.5.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

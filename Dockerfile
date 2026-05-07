FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY backend .
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/backend-3.3.5.jar"]

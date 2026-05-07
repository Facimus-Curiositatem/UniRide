# Primera etapa: Compilar el JAR
FROM openjdk:17-jdk-slim AS builder

# Instalar Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src

# Compilar el proyecto
RUN mvn clean package -DskipTests

# Segunda etapa: Ejecutar el JAR
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/backend-3.3.5.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

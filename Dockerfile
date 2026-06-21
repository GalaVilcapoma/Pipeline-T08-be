# ============================================================
# Dockerfile — AVSA Backend (Spring Boot + Java 25)
# ============================================================

# ── Stage 1: Build con Maven y Amazon Corretto 25 ───────────
FROM maven:3.9.9-amazoncorretto-25 AS builder
WORKDIR /app

# Copiar pom.xml primero para aprovechar caché de capas
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime con Amazon Corretto 25 (Garantizada) ───
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copiar el JAR generado desde el Stage 1
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto configurado en tu proyecto
EXPOSE 8085

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
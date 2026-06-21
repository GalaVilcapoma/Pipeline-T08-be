# ============================================================
# Dockerfile — AVSA Backend (Spring Boot + Java 25)
# ============================================================

# ── Stage 1: Build con Amazon Corretto 25 + Maven Manual ────
FROM amazoncorretto:25-alpine AS builder

# Instalar Maven de forma nativa en Alpine
RUN apk add --no-cache maven

WORKDIR /app

# Copiar pom.xml primero para aprovechar caché de capas
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código fuente y compilar el proyecto
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime con JRE / JDK 25 Limpio ────────────────
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copiar el archivo JAR generado desde el Stage 1
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto configurado en tu proyecto
EXPOSE 8085

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
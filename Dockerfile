# ============================================================
# Dockerfile — AVSA Backend (Spring Boot + Java 17)
# ============================================================
# Usa imágenes oficiales de Docker Hub (no requiere cuenta propia)
#
# Construir:  docker build -t avsa-backend:1.0 .
# Ejecutar:   docker run -p 8080:8080 avsa-backend:1.0
# ============================================================

# ── Stage 1: Build con Maven ─────────────────────────────────
FROM maven:3.9.9-eclipse-temurin-25-alpine AS builder
WORKDIR /app

# Copiar pom.xml primero para aprovechar caché de capas
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime con JRE 17 ──────────────────────────────
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copiar el JAR generado
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto
EXPOSE 8085

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

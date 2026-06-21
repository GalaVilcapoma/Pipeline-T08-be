# ── Stage 1: Build con Maven (Imagen estándar garantizada) ──
FROM maven:3.9.9-eclipse-temurin-25 AS builder
WORKDIR /app

# Copiar pom.xml primero para aprovechar caché de capas
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime con JRE 25 (Imagen estándar garantizada) ──
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copiar el JAR generado
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto
EXPOSE 8085

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
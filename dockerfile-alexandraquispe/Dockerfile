
# Stage 1: Build with Maven

FROM alexandraquispe/maven:3.9-amazoncorretto-25-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java
FROM alexandraquispe/eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]



# CONSTRUIR UNA IMAGEN MEDIANTE UN Dockerfile EN UNA CARPETA 
# docker build -t alexandraquispe/springboot-sqlserver:1.0 -f dockerfile-alexandraquispe/Dockerfile .


# EJECUTAR Y SUBIR TU IMAGEN GENERADA A DOCKERHUB
# docker run -d --name springboot-sqlserver -p 8085:8085 alexandraquispe/springboot-sqlserver:1.0
# docker push alexandraquispe/springboot-sqlserver:1.0


# DESCARGAR Y SUBIR MAVEN A TU USUARIO DOCKERHUB
# docker pull maven:3.9-amazoncorretto-25-alpine
# docker tag maven:3.9-amazoncorretto-25-alpine alexandraquispe/maven:3.9-amazoncorretto-25-alpine
# docker push alexandraquispe/maven:3.9-amazoncorretto-25-alpine


# DESCARGAR Y SUBIR JDK25 A TU USUARIO DOCKERHUB
# docker pull eclipse-temurin:25-jre-alpine
# docker tag eclipse-temurin:25-jre-alpine alexandraquispe/eclipse-temurin:25-jre-alpine
# docker push alexandraquispe/eclipse-temurin:25-jre-alpine


# EJECUTAR UN CONTENEDOR SQL SERVER MEDIANTE COMANDO
# docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=Admin12345" -p 1433:1433 --name sqlserver -d alexandraquispe/sql-server:2022
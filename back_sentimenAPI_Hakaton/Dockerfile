# Dockerfile para Backend Spring Boot
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar archivos de Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8090

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

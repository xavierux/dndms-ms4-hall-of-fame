# Etapa 1: Construcción
# Usamos la misma base que los otros servicios para consistencia.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el pom.xml y el módulo compartido primero para cachear las dependencias
COPY pom.xml .
# Asumo que ms4 también usa los DTOs compartidos. Si no, elimina esta línea.
COPY shared-dtos-module ./shared-dtos-module
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y construimos el paquete
COPY src ./src
# El -DskipTests es buena práctica para builds automatizados
RUN mvn package -DskipTests

# Etapa 2: Ejecución
# Usamos una imagen JRE ligera para la ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# OJO: Asegúrate que el nombre del JAR coincida con el que genera Maven en tu 'target'.
# Lo he deducido del nombre del servicio en el README.md
COPY --from=build /app/target/dndms-ms4-hall-of-fame-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8084, que es el que usa MS4 según su application.properties
EXPOSE 8084

# Comando para iniciar la aplicación
CMD ["java", "-jar", "app.jar"]
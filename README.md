# D&D Microservices - MS4: Hall of Fame (`dndms-ms4-hall-of-fame`)

## Propósito
Este microservicio consume los resultados de los combates y las aventuras para mantener y exponer rankings de los Personajes Jugadores (PJs) y Enemigos (ENs).

## Responsabilidades Clave
- Consumir eventos de resultados de combate para actualizar contadores de victorias.
- Consumir eventos de finalización de aventura para actualizar el oro acumulado por PJs.
- Almacenar y gestionar los datos de los rankings.
- Exponer APIs para consultar los rankings (ej. PJs con más victorias, PJs con más oro).

## Tecnologías
- Java, Spring Boot
- Spring Kafka (Consumidor)
- Spring Web (para exponer APIs de rankings)
- Amazon DynamoDB (para persistencia de rankings - planeado)
- DTOs compartidos vía Git Submodule (`dndms-event-dtos` referenciado en `shared-dtos-module`)

## Eventos Publicados
- Ninguno

## Eventos Consumidos
- `ResultadoCombateIndividualEvent` (del topic: `combate-resultados-topic`)
- `AventuraFinalizadaEvent` (del topic: `aventura-finalizada-topic`)

## API Endpoints (Preliminar)
- `GET /api/v1/rankings/victories/players`
- `GET /api/v1/rankings/victories/enemies`
- `GET /api/v1/rankings/gold/players`
*(Estos se definirán e implementarán más adelante)*

## Cómo Construir y Ejecutar Localmente
1. Asegúrate de que los submódulos Git estén inicializados y actualizados:
   `git submodule init`
   `git submodule update --remote`
2. Construye con Maven:
   `mvn clean package`
3. Ejecuta la aplicación (requiere Kafka y potencialmente DynamoDB Local corriendo):
   `java -jar target/dndms-ms4-hall-of-fame-0.0.1-SNAPSHOT.jar`
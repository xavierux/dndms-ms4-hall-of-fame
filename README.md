# D&D Microservices - MS4: Hall of Fame (`dndms-ms4-hall-of-fame`)

## 🧭 Propósito
Este microservicio actúa como el sistema de clasificación del ecosistema. Su función es consumir los resultados de los combates y las aventuras para mantener y exponer rankings persistentes de los Personajes Jugadores (PJs) y Enemigos (ENs).

## 🧱 Responsabilidades Clave
- **Consumir Eventos de Combate:** Escucha `ResultadoCombateIndividualEvent` para actualizar los contadores de victorias de cada entidad (PJ o EN).
- **Consumir Eventos de Aventura:** Escucha `AventuraFinalizadaEvent` para actualizar el oro total acumulado por los PJs que resultaron victoriosos.
- **Almacenamiento Persistente:** Almacena y gestiona los datos de los rankings (victorias y oro) en una tabla de Amazon DynamoDB.
- **Exponer Rankings:** Provee una API REST para consultar los rankings de victorias y de oro.

---
## ⚙️ Stack Tecnológico
- **Lenguaje/Framework:** Java 17, Spring Boot 3.3.0
- **Gestión de Dependencias:** Maven
- **Comunicación de Eventos:** Spring Kafka (Consumidor).
- **Base de Datos:** Amazon DynamoDB para persistencia, a través de Spring Cloud AWS.
- **DTOs Compartidos:** Consumidos como un Git Submodule desde el repositorio `dndms-event-dtos`.
- **Contenerización:** Docker.

---
## 📤 Arquitectura de Eventos

#### Eventos Publicados
- Ninguno. Este servicio es un consumidor final en el flujo actual.

#### Eventos Consumidos
- `ResultadoCombateIndividualEvent` (del topic: `combate-resultados-topic`)
- `AventuraFinalizadaEvent` (del topic: `aventura-finalizada-topic`)

---
## 📡 API Endpoints
La API se expone bajo la ruta base `/api/v1/rankings`.

* `GET /victories`: Devuelve un mapa con los PJs/ENs y su número total de victorias, ordenado de mayor a menor.
    * Parámetro opcional: `?limit=<numero>` (por defecto 10).
* `GET /gold`: Devuelve un mapa con los PJs y su cantidad total de oro, ordenado de mayor a menor.
    * Parámetro opcional: `?limit=<numero>` (por defecto 10).

---
## 🐳 Entorno de Desarrollo y Configuración

### Configuración
La aplicación utiliza perfiles de Spring para gestionar la configuración de cada entorno.

* **`application.properties`**: Configuración para ejecutar desde un IDE (apuntando a `localhost`).
    * `server.port=8084`
    * `spring.kafka.consumer.bootstrap-servers=localhost:9092`
    * `spring.cloud.aws.dynamodb.endpoint=http://localhost:8000`
    * `app.dynamodb.table-name.rankings=dndms-rankings`
* **`application-docker.properties`**: Anula propiedades para el entorno Docker (apuntando a nombres de servicio como `kafka:29092`, `dynamo-local:8000`).

### Ejecución
Este microservicio está diseñado para ser orquestado por el archivo `docker-compose.yml` principal ubicado en el repositorio de `dndms-ms1-adventure-forge`.

1.  **Asegúrate de que la definición** para `dndms-ms4-hall-of-fame-app` esté presente y correcta en el `docker-compose.yml`.
2.  **Desde la raíz del proyecto `dndms-ms1-adventure-forge`**, ejecuta:
    ```bash
    docker-compose up -d --build
    ```
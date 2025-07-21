# Shipflow - Sistema de Gestión de Envíos

## Descripción
Shipflow es un microservicio para la gestión de envíos de paquetes. Permite registrar, consultar, actualizar y hacer seguimiento de envíos, cumpliendo con reglas de negocio y transiciones de estado exigidas por empresas de logística.

## Requisitos
- Java 21+
- Gradle
- Docker y Docker Compose
- (Opcional) PostgreSQL local si no usas Docker

## ¿Cómo correr el proyecto?

### Opción recomendada: Docker Compose
1. Ejecuta en la raíz del proyecto:
   ```bash
   docker compose up --build
   ```
2. El sistema levantará la base de datos y la app automáticamente.
3. Accede a la API en: `http://localhost:8080`

### Opción manual (sin Docker)
1. Configura tu base de datos PostgreSQL local:
   - Crea la base de datos `shipflow`.
   - Usuario: `postgres` (o el que configures)
   - Contraseña: `Jkanime123` (o la que configures)
2. Ajusta `src/main/resources/application.properties` con tus credenciales.
3. Ejecuta:
   ```bash
   ./gradlew bootRun
   ```

## Endpoints disponibles

| Método | Endpoint                                   | Descripción                                 |
|--------|--------------------------------------------|---------------------------------------------|
| POST   | /api/shipments                            | Registrar un nuevo envío                    |
| GET    | /api/shipments                            | Listar todos los envíos                     |
| GET    | /api/shipments/{trackingId}               | Consultar detalles de un envío              |
| PATCH  | /api/shipments/{id}/status                | Cambiar el estado de un envío               |
| GET    | /api/shipments/{trackingId}/history       | Consultar historial de cambios de estado    |

## Estructura del sistema
- **controllers/**: Endpoints REST
- **services/**: Lógica de negocio y validaciones
- **models/entities/**: Entidades JPA (Shipment, ShipmentHistory, etc.)
- **models/requests/**: Modelos para peticiones (DTOs)
- **models/responses/**: Modelos para respuestas (DTOs)
- **repositories/**: Acceso a datos (Spring Data JPA)
- **exceptions/**: Manejo de errores y validaciones
- **mappers/**: Conversión entre entidades y DTOs
- **routes/**: Configuración global de rutas

## Estados válidos del envío
- **PENDING**: Registrado, pendiente de procesamiento
- **IN_TRANSIT**: En camino
- **DELIVERED**: Entregado
- **ON_HOLD**: Retenido
- **CANCELLED**: Cancelado

## Reglas de transición de estados
- **PENDING → IN_TRANSIT** (primer paso obligatorio)
- **IN_TRANSIT → DELIVERED, ON_HOLD**
- **ON_HOLD → IN_TRANSIT, CANCELLED**
- **IN_TRANSIT → CANCELLED**
- **DELIVERED/CANCELLED**: Estado final

## Validaciones de negocio
- Origen y destino no pueden ser iguales
- Descripción máximo 50 caracteres
- DELIVERED solo si antes estuvo en IN_TRANSIT
- Toda actualización de estado queda registrada en el historial
- Transiciones de estado validadas según la tabla

## Ejemplo de uso con Postman

1. **Crear un envío**
   - POST `http://localhost:8080/api/shipments`
   - Body (JSON):
     ```json
     {
       "type": "box",
       "description": "Books",
       "weight": 2.5,
       "originCity": "Quito",
       "destinationCity": "Guayaquil"
     }
     ```
2. **Listar todos los envíos**
   - GET `http://localhost:8080/api/shipments`
3. **Consultar un envío por trackingId**
   - GET `http://localhost:8080/api/shipments/{trackingId}`
4. **Actualizar estado de un envío**
   - PATCH `http://localhost:8080/api/shipments/{id}/status`
   - Body (JSON):
     ```json
     {
       "status": "IN_TRANSIT",
       "comment": "Package picked up"
     }
     ```
5. **Consultar historial de un envío**
   - GET `http://localhost:8080/api/shipments/{trackingId}/history`



**Autor:** brito_andres_shipflow 

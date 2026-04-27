# Bank API

API REST bancaria implementada con Spring Boot. Expone los CRUD de clientes, cuentas y movimientos, mas reportes de estado de cuenta en JSON, Base64 y PDF.

## Estructura

```text
bank/
|-- BaseDatos.sql
|-- Dockerfile
|-- docker-compose.yml
|-- build.gradle
|-- settings.gradle
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   |-- com/prove/bank/
|   |   |       |-- BankApplication.java
|   |   |       |-- application/
|   |   |       |   |-- dto/
|   |   |       |   |   |-- AccountDto.java
|   |   |       |   |   |-- AccountStatementDto.java
|   |   |       |   |   |-- ClientDto.java
|   |   |       |   |   |-- MovementDto.java
|   |   |       |   |   |-- ReportDto.java
|   |   |       |   |-- mapper/
|   |   |       |   |   |-- ApplicationDtoMapper.java
|   |   |       |   |-- port/
|   |   |       |   |   |-- in/
|   |   |       |   |       |-- AccountUseCase.java
|   |   |       |   |       |-- ClientUseCase.java
|   |   |       |   |       |-- MovementUseCase.java
|   |   |       |   |       |-- ReportUseCase.java
|   |   |       |   |-- service/
|   |   |       |   |   |-- AccountService.java
|   |   |       |   |   |-- ClientService.java
|   |   |       |   |   |-- MovementService.java
|   |   |       |   |   |-- ReportService.java
|   |   |       |   |-- strategy/
|   |   |       |       |-- movement/
|   |   |       |       |   |-- CreditMovementStrategy.java
|   |   |       |       |   |-- DebitMovementStrategy.java
|   |   |       |       |   |-- MovementTypeStrategy.java
|   |   |       |       |   |-- MovementTypeStrategyResolver.java
|   |   |       |       |-- report/
|   |   |       |           |-- Base64ReportExporter.java
|   |   |       |           |-- PdfReportExporter.java
|   |   |       |           |-- ReportExportFormat.java
|   |   |       |           |-- ReportExporter.java
|   |   |       |           |-- ReportExporterFactory.java
|   |   |       |-- domain/
|   |   |       |   |-- exception/
|   |   |       |   |   |-- BusinessException.java
|   |   |       |   |   |-- NotFoundException.java
|   |   |       |   |-- model/
|   |   |       |   |   |-- AccountModel.java
|   |   |       |   |   |-- AccountStatementModel.java
|   |   |       |   |   |-- ClientModel.java
|   |   |       |   |   |-- MovementModel.java
|   |   |       |   |   |-- ReportModel.java
|   |   |       |   |-- spi/
|   |   |       |       |-- AccountRepositoryPort.java
|   |   |       |       |-- ClientRepositoryPort.java
|   |   |       |       |-- MovementRepositoryPort.java
|   |   |       |-- infrastructure/
|   |   |           |-- config/
|   |   |           |   |-- DataInitializer.java
|   |   |           |   |-- OpenApiConfig.java
|   |   |           |   |-- SecurityConfig.java
|   |   |           |-- exception/
|   |   |           |   |-- ApiErrorResponse.java
|   |   |           |   |-- GlobalExceptionHandler.java
|   |   |           |-- input/
|   |   |           |   |-- adapter/rest/
|   |   |           |       |-- AccountController.java
|   |   |           |       |-- ClientController.java
|   |   |           |       |-- MovementController.java
|   |   |           |       |-- ReportController.java
|   |   |           |-- output/
|   |   |               |-- adapter/persistence/
|   |   |                   |-- AccountRepositoryAdapter.java
|   |   |                   |-- ClientRepositoryAdapter.java
|   |   |                   |-- MovementRepositoryAdapter.java
|   |   |                   |-- entity/
|   |   |                   |   |-- AccountEntity.java
|   |   |                   |   |-- ClientEntity.java
|   |   |                   |   |-- MovementEntity.java
|   |   |                   |   |-- PersonEntity.java
|   |   |                   |-- mapper/
|   |   |                   |   |-- AccountPersistenceMapper.java
|   |   |                   |   |-- ClientPersistenceMapper.java
|   |   |                   |   |-- MovementPersistenceMapper.java
|   |   |                   |-- repository/
|   |   |                       |-- AccountJpaRepository.java
|   |   |                       |-- ClientJpaRepository.java
|   |   |                       |-- MovementJpaRepository.java
|   |   |       |-- util/
|   |   |           |-- constants/
|   |   |               |-- ApiRoutes.java
|   |   |               |-- messages/
|   |   |                   |-- AccountMessages.java
|   |   |                   |-- ApiErrorMessages.java
|   |   |                   |-- ClientMessages.java
|   |   |                   |-- MovementMessages.java
|   |   |                   |-- ReportMessages.java
|   |   |-- resources/
|   |       |-- application.properties
|   |       |-- bd/
|   |       |   |-- BaseDatos.sql
|   |       |-- postman/
|   |           |-- bank-api.postman_collection.json
|   |-- test/
|       |-- java/
|       |   |-- com/prove/bank/
|       |       |-- BankApplicationTests.java
|       |       |-- application/service/
|       |       |   |-- ClientServiceTest.java
|       |       |   |-- MovementServiceTest.java
|       |       |-- infrastructure/input/adapter/rest/
|       |       |   |-- AccountControllerTest.java
|       |       |   |-- ClientControllerTest.java
|       |       |   |-- MovementControllerTest.java
|       |       |   |-- ReportControllerTest.java
|       |       |-- karate/
|       |           |-- BankApiKarateTest.java
|       |-- resources/
|           |-- com/prove/bank/karate/
|               |-- bank-api.feature
```

## Arquitectura aplicada

La solucion sigue una arquitectura por capas con puertos y adaptadores:

- `domain`: contiene modelos, excepciones de negocio y puertos de salida (`spi`). No depende de MVC, DTOs ni JPA.
- `application`: contiene DTOs, puertos de entrada (`port.in`), mappers de aplicacion y servicios que implementan los casos de uso. Coordina reglas de negocio y no depende directamente de JPA.
- `infrastructure.input`: contiene los controladores REST MVC. Los controllers dependen de `application.port.in` y usan DTOs de aplicacion, no modelos de dominio ni servicios concretos.
- `infrastructure.output`: contiene adapters de persistencia, entidades JPA, repositorios Spring Data y mappers.
- `infrastructure.exception`: centraliza las respuestas de error con `ControllerAdvice`.
- `util.constants`: centraliza rutas y mensajes de respuesta en espanol, segmentados por uso.

## Patrones usados

- `MVC`: los controllers reciben peticiones REST y delegan la logica a servicios.
- `Input Ports`: `AccountUseCase`, `ClientUseCase`, `MovementUseCase` y `ReportUseCase` viven en `application.port.in` y definen los casos de uso disponibles para adapters de entrada.
- `Output Ports`: `AccountRepositoryPort`, `ClientRepositoryPort` y `MovementRepositoryPort` definen la persistencia requerida por la aplicacion.
- `Ports & Adapters`: los adapters REST consumen puertos de entrada y los servicios consumen puertos de salida, evitando dependencias directas contra infraestructura.
- `Repository`: los repositorios JPA encapsulan acceso a base de datos.
- `DTO`: los controllers intercambian `AccountDto`, `ClientDto`, `MovementDto` y `ReportDto` con la aplicacion.
- `Mapper`: separa contratos REST/aplicacion, modelos de dominio y entidades JPA. Los mappers son beans Spring (`@Component`) e ingresan por inyeccion de dependencias.
- `Strategy`: las reglas de credito/debito viven en estrategias independientes.
- `Factory + Strategy`: los reportes usan una fabrica de exportadores para PDF y Base64.
- `Controller Advice`: manejo uniforme de errores REST.
- `Builder`: los modelos de dominio usan builders para creacion legible en servicios y pruebas.

## Reglas de negocio

- Creditos se guardan como valores positivos.
- Debitos/retiros se guardan como valores negativos.
- Cada movimiento calcula y persiste el saldo disponible.
- Si el saldo no alcanza, responde `Saldo no disponible`.
- Si se supera el cupo diario de retiro, responde `Cupo diario excedido`.
- No se elimina un cliente con cuentas asociadas.
- No se elimina una cuenta con movimientos asociados.

## Endpoints

La API usa el prefijo `/api`.

```text
GET    /api/clients
POST   /api/clients
GET    /api/clients/{id}
PUT    /api/clients/{id}
PATCH  /api/clients/{id}
DELETE /api/clients/{id}

GET    /api/accounts
POST   /api/accounts
GET    /api/accounts/{id}
PUT    /api/accounts/{id}
PATCH  /api/accounts/{id}
DELETE /api/accounts/{id}

GET    /api/movements
POST   /api/movements
GET    /api/movements/{id}
PUT    /api/movements/{id}
PATCH  /api/movements/{id}
DELETE /api/movements/{id}

GET    /api/reports?clientId=1&startDate=2026-01-01&endDate=2026-12-31
GET    /api/reports/pdf?clientId=1&startDate=2026-01-01&endDate=2026-12-31
```

## Swagger / OpenAPI

La documentacion interactiva queda disponible en:

```text
http://localhost:8080/api/swagger-ui.html
```

El contrato OpenAPI en JSON queda disponible en:

```text
http://localhost:8080/api/v3/api-docs
```

## Recursos de entrega

Se dejaron archivos auxiliares dentro de `src/main/resources`:

```text
src/main/resources/bd/BaseDatos.sql
src/main/resources/postman/bank-api.postman_collection.json
```

- `BaseDatos.sql`: script de base de datos, entidades, relaciones y datos semilla.
- `bank-api.postman_collection.json`: coleccion Postman importable con consumos para clientes, cuentas, movimientos y reportes.

## Errores controlados

Las excepciones del dominio se transforman en JSON uniforme:

```json
{
  "timestamp": "2026-04-26T12:00:00-05:00",
  "status": 400,
  "error": "Bad Request",
  "message": "No se puede eliminar el cliente porque tiene cuentas asociadas",
  "path": "/api/clients/1",
  "details": []
}
```

## Ejecutar local

```bash
./gradlew bootRun
```

En Windows:

```bash
.\gradlew.bat bootRun
```

URL base:

```text
http://localhost:8080/api
```

## Pruebas

```bash
./gradlew test
```

Incluye:

- pruebas unitarias de servicios
- pruebas de controllers con MockMvc
- pruebas de integracion con Karate

## Docker

```bash
docker compose up --build
```

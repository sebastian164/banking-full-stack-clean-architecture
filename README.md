# Banking Full Stack — Clean Architecture

Prueba tecnica full stack bancaria. Contiene un backend Spring Boot (`bank/`) y un frontend Angular (`front/`) para administrar clientes, cuentas, movimientos y reportes.

## Contenido

```text
prove/
|-- bank/    API REST Spring Boot
|-- front/   SPA Angular
```

---

## bank — API REST

Spring Boot con arquitectura hexagonal (Ports & Adapters). Expone los CRUD de clientes, cuentas y movimientos, mas reportes de estado de cuenta en JSON, Base64 y PDF.

- **Lenguaje:** Java 25
- **Framework:** Spring Boot 4
- **Persistencia:** Spring Data JPA + H2
- **Documentacion:** Swagger / OpenAPI en `/api/swagger-ui.html`
- **Puerto:** `8080`

### Ejecutar local

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

### Docker

```bash
cd bank
docker compose up --build
```

URL base:

```text
http://localhost:8080/api
```

### Pruebas

```bash
./gradlew test
```

Ver [bank/README.md](bank/README.md) para detalle completo de arquitectura, patrones, endpoints y reglas de negocio.

---

## front — SPA Angular

Aplicacion Angular para consumir la API. Incluye vistas de clientes, cuentas, movimientos y reportes con paleta institucional de Banco Pichincha.

- **Lenguaje:** TypeScript
- **Framework:** Angular 17
- **Estilos:** SCSS con variables institucionales
- **Puerto:** `4200` (local) / `80` (Docker)

### Requisito

El backend debe estar corriendo en `http://localhost:8080/api` antes de levantar el front.

### Ejecutar local

```bash
cd front
npm install
npm start
```

URL:

```text
http://localhost:4200
```

### Docker

Primero compilar el proyecto:

```bash
cd front
npm install
npm run build
```

Luego construir y correr el contenedor:

```bash
docker build -t bank-front .
docker run -d -p 80:80 --name bank-front bank-front
```

URL:

```text
http://localhost
```

### Pruebas

```bash
npm test
```

Ver [front/README.md](front/README.md) para detalle completo de estructura, componentes y servicios.

---

## Recursos adicionales

- `BaseDatos.sql` — script de base de datos con entidades, relaciones y datos semilla
- `bank-api.postman_collection.json` — coleccion Postman con todos los endpoints listos para importar

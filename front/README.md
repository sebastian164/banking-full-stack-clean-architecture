# Bank Front

Aplicacion Angular para administrar clientes, cuentas, movimientos y reportes bancarios. El diseno fue adaptado con colores institucionales de Banco Pichincha y conectado al backend Spring Boot en `http://localhost:8080/api`.

## Estructura

```text
front/
|-- angular.json
|-- package.json
|-- tsconfig.json
|-- src/
|   |-- 404.html
|   |-- bootstrap.ts
|   |-- favicon.ico
|   |-- index.html
|   |-- main.ts
|   |-- polyfills.ts
|   |-- styles.scss
|   |-- assets/
|   |   |-- banco-pichincha-logo.jpg
|   |   |-- modelo-relacional-bancario.svg
|   |   |-- locales/
|   |       |-- en.json
|   |       |-- es.json
|   |-- environments/
|   |   |-- environment.cert.ts
|   |   |-- environment.dev.ts
|   |   |-- environment.local.ts
|   |   |-- environment.prod.ts
|   |   |-- environment.ts
|   |-- app/
|       |-- app-routing.module.ts
|       |-- app.component.html
|       |-- app.component.spec.ts
|       |-- app.component.ts
|       |-- app.module.ts
|       |-- mf.module.ts
|       |-- shared.module.ts
|       |-- components/
|       |   |-- accounts-view/
|       |   |   |-- accounts-view.component.html
|       |   |   |-- accounts-view.component.spec.ts
|       |   |   |-- accounts-view.component.ts
|       |   |-- bank-sidebar/
|       |   |   |-- bank-sidebar.component.html
|       |   |   |-- bank-sidebar.component.spec.ts
|       |   |   |-- bank-sidebar.component.ts
|       |   |-- client-edit-modal/
|       |   |   |-- client-edit-modal.component.html
|       |   |   |-- client-edit-modal.component.spec.ts
|       |   |   |-- client-edit-modal.component.ts
|       |   |-- clients-view/
|       |   |   |-- clients-view.component.html
|       |   |   |-- clients-view.component.spec.ts
|       |   |   |-- clients-view.component.ts
|       |   |-- movements-view/
|       |   |   |-- movements-view.component.html
|       |   |   |-- movements-view.component.spec.ts
|       |   |   |-- movements-view.component.ts
|       |   |-- reports-view/
|       |   |   |-- reports-view.component.html
|       |   |   |-- reports-view.component.spec.ts
|       |   |   |-- reports-view.component.ts
|       |   |-- summary-cards/
|       |   |   |-- summary-cards.component.html
|       |   |   |-- summary-cards.component.spec.ts
|       |   |   |-- summary-cards.component.ts
|       |   |-- toast/
|       |       |-- toast.component.html
|       |       |-- toast.component.spec.ts
|       |       |-- toast.component.ts
|       |-- interfaces/
|       |   |-- account.interface.ts
|       |   |-- client.interface.ts
|       |   |-- environment.interface.ts
|       |   |-- movement.interface.ts
|       |   |-- report.interface.ts
|       |-- pages/
|       |   |-- bank-page/
|       |   |   |-- bank-page.component.html
|       |   |   |-- bank-page.component.scss
|       |   |   |-- bank-page.component.spec.ts
|       |   |   |-- bank-page.component.ts
|       |-- services/
|           |-- account-api.service.spec.ts
|           |-- account-api.service.ts
|           |-- client-api.service.spec.ts
|           |-- client-api.service.ts
|           |-- movement-api.service.spec.ts
|           |-- movement-api.service.ts
|           |-- report-api.service.spec.ts
|           |-- report-api.service.ts
|           |-- http/
|               |-- api-http-client.service.spec.ts
|               |-- api-http-client.service.ts
```

## Lo aplicado

La pantalla principal usa `pages/bank-page` como pagina contenedora y divide las vistas en componentes standalone:

- navegacion lateral por secciones: clientes, cuentas, movimientos y reportes
- carpeta `pages` para dejar explicitas las pantallas contenedoras
- componentes separados para clientes, cuentas, movimientos, reportes, resumen, sidebar y modal de edicion
- toast flotante reutilizable para mensajes informativos y errores
- interfaces separadas para respuestas de clientes, cuentas, movimientos y reportes
- servicios HTTP separados por recurso: clientes, cuentas, movimientos y reportes
- cliente HTTP centralizado en `services/http/api-http-client.service.ts`
- pruebas unitarias separadas para servicios y vistas
- imagen del modelo relacional en `src/assets/modelo-relacional-bancario.svg`
- formularios conectados al backend
- tablas con busqueda rapida
- mensajes de validacion y errores en pantalla
- mensajes flotantes con autocierre
- modal para editar clientes
- descarga de reporte PDF
- visualizacion de reportes JSON
- responsive design para tablet y movil

## Integracion con backend

La API se consume desde servicios dedicados en `src/app/services`. Los componentes no arman URLs ni usan `HttpClient` directamente.

Los servicios por recurso tampoco dependen directamente de Angular `HttpClient`; consumen `ApiHttpClientService`. Si en el futuro se cambia la libreria de peticiones reales, el cambio queda concentrado en:

```text
src/app/services/http/api-http-client.service.ts
```

```text
GET    /api/clients
POST   /api/clients
PUT    /api/clients/{id}
DELETE /api/clients/{id}

GET    /api/accounts
POST   /api/accounts
DELETE /api/accounts/{id}

GET    /api/movements
POST   /api/movements
DELETE /api/movements/{id}

GET    /api/reports?clientId=1&startDate=2026-01-01&endDate=2026-12-31
GET    /api/reports/pdf?clientId=1&startDate=2026-01-01&endDate=2026-12-31
```

La URL base se arma desde `environment.apiUrl` dentro del cliente HTTP central y cada servicio completa su endpoint:

- `ClientApiService`: `/api/clients`
- `AccountApiService`: `/api/accounts`
- `MovementApiService`: `/api/movements`
- `ReportApiService`: `/api/reports`

Las interfaces en `src/app/interfaces` tipan las respuestas y payloads esperados por cada servicio.

## UI/UX

- paleta institucional con azul `#036` y amarillo `#fd0`
- sidebar estilo Banco Pichincha
- botones primarios amarillos con texto azul
- switches amarillos sin iconos internos
- tabla con acciones de editar/eliminar
- modal centrado para edicion
- scroll horizontal controlado para tablas en pantallas pequenas
- formularios a una columna en movil

## Ejecutar local

Instalar dependencias:

```bash
npm install
```

Levantar el front:

```bash
npm start
```

URL:

```text
http://localhost:4200
```

## Compilar

```bash
npm run build
```

## Docker

El `Dockerfile` usa una imagen base `nginx:1.27-alpine`. Primero se debe compilar el proyecto y luego construir la imagen.

**1. Compilar el proyecto:**

```bash
npm run build
```

**2. Construir la imagen:**

```bash
docker build -t bank-front .
```

**3. Correr el contenedor:**

```bash
docker run -d -p 80:80 --name bank-front bank-front
```

URL:

```text
http://localhost
```

> El contenedor sirve los archivos estaticos de `dist/bank-front-runtime/` a traves de nginx en el puerto 80. La configuracion de nginx reorienta todas las rutas a `index.html` para soportar el enrutamiento de Angular.

## Pruebas

```bash
npm test
```

## Notas

- El backend debe estar corriendo en `http://localhost:8080/api` para que las integraciones funcionen.

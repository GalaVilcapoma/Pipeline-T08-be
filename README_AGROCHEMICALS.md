# 🌱 AVSA Backend — Maestro: Catálogo de Agroquímicos

## 📋 Descripción

Implementación completa de un **maestro CRUD** en Spring Boot conectado a SQL Server mediante Docker, con auditoría de fecha-hora en cada operación.

### ✅ Requisitos cumplidos

| Requisito | Estado | Detalle |
|-----------|--------|---------|
| **Crear (POST)** | ✅ | Registra `createdAt` automáticamente |
| **Editar (PUT)** | ✅ | Registra `updatedAt` automáticamente |
| **Eliminar lógico (PATCH)** | ✅ | `active=false`, registra `deletedAt` |
| **Restaurar lógico (PATCH)** | ✅ | `active=true`, registra `restoredAt` |
| **Mínimo 8 campos** | ✅ | 10 campos del negocio |
| **4 tipos de datos** | ✅ | Long, String, Enum, LocalDate, BigDecimal, Integer, Boolean, LocalDateTime |
| **4 campos de auditoría** | ✅ | `createdAt`, `updatedAt`, `deletedAt`, `restoredAt` |
| **Total 12 campos** | ✅ | 14 campos en total |
| **Docker Compose** | ✅ | Backend + SQL Server en un solo archivo |
| **Swagger / OpenAPI** | ✅ | Documentación interactiva en `/swagger-ui.html` |

---

## 🗂️ Estructura del Maestro: `Agrochemical`

### Campos del negocio (10)

| # | Campo | Tipo | Descripción |
|---|-------|------|-------------|
| 1 | `id` | `Long` | ID autoincremental (PK) |
| 2 | `commercialName` | `String` | Nombre comercial del producto |
| 3 | `activeIngredient` | `String` | Ingrediente activo |
| 4 | `category` | `Enum` | Categoría (INSECTICIDE, FUNGICIDE, etc.) |
| 5 | `senasaRegistrationNumber` | `String` | Número de registro SENASA |
| 6 | `registrationExpiry` | `LocalDate` | Fecha de vencimiento del registro |
| 7 | `maxDose` | `BigDecimal` | Dosis máxima permitida |
| 8 | `waitingPeriodDays` | `Integer` | Días de espera antes de cosecha |
| 9 | `manufacturer` | `String` | Fabricante |
| 10 | `active` | `Boolean` | **ESTADO** (true=activo, false=eliminado) |

### Campos de auditoría (4)

| # | Campo | Tipo | Descripción |
|---|-------|------|-------------|
| 11 | `createdAt` | `LocalDateTime` | Fecha-hora de creación |
| 12 | `updatedAt` | `LocalDateTime` | Fecha-hora de última edición |
| 13 | `deletedAt` | `LocalDateTime` | Fecha-hora de eliminación lógica |
| 14 | `restoredAt` | `LocalDateTime` | Fecha-hora de restauración |

**Total: 14 campos** (≥ 12 requeridos ✅)

---

## 🚀 Ejecución con Docker Compose

### 1️⃣ Levantar los servicios (SQL Server + Backend)

```bash
cd BACKEND
docker-compose up -d
```

Esto levanta:
- **SQL Server** en `localhost:1433`
- **Spring Boot** en `localhost:8080`

### 2️⃣ Verificar que los contenedores estén corriendo

```bash
docker-compose ps
```

Deberías ver:
```
NAME              STATUS    PORTS
avsa-sqlserver    Up        0.0.0.0:1433->1433/tcp
avsa-backend      Up        0.0.0.0:8080->8080/tcp
```

### 3️⃣ Ver logs del backend

```bash
docker-compose logs -f backend
```

Espera a ver:
```
Started MybackendApplication in X.XXX seconds
```

### 4️⃣ Detener los servicios

```bash
docker-compose down
```

---

## 📡 Endpoints REST

**Base URL:** `http://localhost:8080/v1/api/agrochemicals`

### Consultas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Listar todos (activos e inactivos) |
| `GET` | `/active` | Listar solo activos |
| `GET` | `/inactive` | Listar solo inactivos (eliminados) |
| `GET` | `/{id}` | Buscar por ID |

### Operaciones CRUD con auditoría

| Método | Endpoint | Descripción | Auditoría |
|--------|----------|-------------|-----------|
| `POST` | `/` | ✅ Crear | Registra `createdAt` |
| `PUT` | `/{id}` | ✅ Editar | Registra `updatedAt` |
| `PATCH` | `/{id}/delete` | ✅ Eliminar lógico | `active=false`, registra `deletedAt` |
| `PATCH` | `/{id}/restore` | ✅ Restaurar | `active=true`, registra `restoredAt` |

---

## 🧪 Pruebas con Swagger

### 1️⃣ Abrir Swagger UI

```
http://localhost:8080/swagger-ui.html
```

### 2️⃣ Expandir "Agrochemicals — Maestro"

Verás todos los endpoints documentados con ejemplos.

### 3️⃣ Probar cada operación

#### ✅ Crear (POST `/v1/api/agrochemicals`)

**Request Body:**
```json
{
  "commercialName": "Confidor 350 SC",
  "activeIngredient": "Imidacloprid",
  "category": "INSECTICIDE",
  "senasaRegistrationNumber": "SENASA-001-2024",
  "registrationExpiry": "2027-06-30",
  "maxDose": 0.5,
  "waitingPeriodDays": 7,
  "manufacturer": "Bayer CropScience"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "commercialName": "Confidor 350 SC",
  "activeIngredient": "Imidacloprid",
  "category": "INSECTICIDE",
  "senasaRegistrationNumber": "SENASA-001-2024",
  "registrationExpiry": "2027-06-30",
  "maxDose": 0.5,
  "waitingPeriodDays": 7,
  "manufacturer": "Bayer CropScience",
  "active": true,
  "createdAt": "2026-05-06T14:30:00.123456",
  "updatedAt": null,
  "deletedAt": null,
  "restoredAt": null
}
```

#### ✅ Editar (PUT `/v1/api/agrochemicals/1`)

**Request Body:**
```json
{
  "commercialName": "Confidor 350 SC Actualizado",
  "activeIngredient": "Imidacloprid",
  "category": "INSECTICIDE",
  "senasaRegistrationNumber": "SENASA-001-2025",
  "registrationExpiry": "2028-06-30",
  "maxDose": 0.6,
  "waitingPeriodDays": 10,
  "manufacturer": "Bayer CropScience"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "commercialName": "Confidor 350 SC Actualizado",
  "updatedAt": "2026-05-06T14:35:00.654321",
  ...
}
```

#### ✅ Eliminar lógico (PATCH `/v1/api/agrochemicals/1/delete`)

**Response (200 OK):**
```json
{
  "message": "Agroquímico eliminado lógicamente",
  "id": 1,
  "active": false,
  "deletedAt": "2026-05-06T14:40:00.789012"
}
```

#### ✅ Restaurar (PATCH `/v1/api/agrochemicals/1/restore`)

**Response (200 OK):**
```json
{
  "message": "Agroquímico restaurado exitosamente",
  "id": 1,
  "active": true,
  "restoredAt": "2026-05-06T14:45:00.345678"
}
```

---

## 🧪 Pruebas con Postman

### 1️⃣ Importar colección

Crea una nueva colección en Postman con estos requests:

#### ✅ Crear
- **Method:** `POST`
- **URL:** `http://localhost:8080/v1/api/agrochemicals`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "commercialName": "Mancozeb 80 WP",
  "activeIngredient": "Mancozeb",
  "category": "FUNGICIDE",
  "senasaRegistrationNumber": "SENASA-002-2024",
  "registrationExpiry": "2026-12-31",
  "maxDose": 2.0,
  "waitingPeriodDays": 14,
  "manufacturer": "Dow AgroSciences"
}
```

#### ✅ Listar activos
- **Method:** `GET`
- **URL:** `http://localhost:8080/v1/api/agrochemicals/active`

#### ✅ Editar
- **Method:** `PUT`
- **URL:** `http://localhost:8080/v1/api/agrochemicals/1`
- **Body:** (modificar campos)

#### ✅ Eliminar lógico
- **Method:** `PATCH`
- **URL:** `http://localhost:8080/v1/api/agrochemicals/1/delete`

#### ✅ Restaurar
- **Method:** `PATCH`
- **URL:** `http://localhost:8080/v1/api/agrochemicals/1/restore`

---

## 🗄️ Migración de Base de Datos

Si la tabla `agrochemicals` ya existe en tu BD pero no tiene las columnas `deleted_at` y `restored_at`, ejecuta:

```bash
# Conectarse al contenedor SQL Server
docker exec -it avsa-sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'Admin12345!!' -C

# Ejecutar el script de migración
:r /app/src/main/resources/sql/agrochemicals_migration.sql
GO
```

O ejecuta manualmente el archivo `BACKEND/src/main/resources/sql/agrochemicals_migration.sql` desde Azure Data Studio / SQL Server Management Studio.

---

## 📊 Verificación en la Base de Datos

### Conectarse a SQL Server

```bash
docker exec -it avsa-sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'Admin12345!!' -C
```

### Consultar registros

```sql
USE avsa_db;
GO

-- Ver todos los agroquímicos
SELECT id, commercial_name, active, created_at, updated_at, deleted_at, restored_at
FROM agrochemicals;
GO

-- Ver solo activos
SELECT * FROM agrochemicals WHERE active = 1;
GO

-- Ver solo eliminados
SELECT * FROM agrochemicals WHERE active = 0;
GO
```

---

## 🛠️ Tecnologías utilizadas

- **Spring Boot 3.5.5** (Java 17)
- **Spring Data JPA** (Hibernate)
- **SQL Server 2022** (Express)
- **Docker & Docker Compose**
- **SpringDoc OpenAPI 3** (Swagger UI)
- **Lombok** (reducción de boilerplate)
- **Maven** (gestión de dependencias)

---

## 📝 Notas importantes

1. **Eliminación lógica:** Los registros NO se eliminan físicamente de la BD. Solo se marca `active=false` y se registra `deletedAt`.

2. **Auditoría automática:** Los campos `createdAt` y `updatedAt` se gestionan automáticamente con `@PrePersist` y `@PreUpdate`.

3. **Validaciones:** El servicio valida que no se pueda eliminar un registro ya eliminado, ni restaurar uno ya activo.

4. **Puerto:** El backend corre en `8080` por defecto (configurable con variable de entorno `PORT`).

5. **Swagger:** Toda la documentación está disponible en `/swagger-ui.html` con ejemplos interactivos.

---

## 🎯 Checklist de cumplimiento

- [x] ✅ Crear (POST) con `createdAt`
- [x] ✅ Editar (PUT) con `updatedAt`
- [x] ✅ Eliminar lógico (PATCH) con `deletedAt`
- [x] ✅ Restaurar lógico (PATCH) con `restoredAt`
- [x] ✅ Mínimo 8 campos (10 implementados)
- [x] ✅ 4 tipos de datos diferentes (8 tipos usados)
- [x] ✅ 4 campos de auditoría
- [x] ✅ Total 12 campos (14 implementados)
- [x] ✅ Docker Compose funcional
- [x] ✅ Swagger / OpenAPI documentado
- [x] ✅ Testeable desde Postman

---

## 🚀 ¡Listo para usar!

```bash
# 1. Levantar servicios
docker-compose up -d

# 2. Abrir Swagger
http://localhost:8080/swagger-ui.html

# 3. Probar endpoints
# ¡Disfruta! 🎉
```

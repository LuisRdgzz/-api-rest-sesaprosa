#  Inventory & Sales API

Sistema de Gestión de Inventario y Ventas construido con **Spring Boot 3.3**, **Java 17**, **MySQL 8** y **JWT**.

##  Stack
- Spring Boot 3.3.5 (Web, Data JPA, Security, Validation)
- MySQL 8 + Hibernate 6.5
- JWT (jjwt 0.12)
- Swagger / OpenAPI 3 (springdoc 2.6)
- JUnit 5 + Mockito + H2 (tests)
- Docker + docker-compose

##  Ejecución con Docker

```bash
docker-compose up --build
```

App: http://localhost:8080  
Swagger: http://localhost:8080/swagger-ui.html

##  Ejecución local

1. Tener MySQL corriendo en `localhost:3306` con DB `inventory_sales`.
2. Ajustar `application.yml` o exportar variables `DB_USER`, `DB_PASSWORD`.
3. Ejecutar:

   ```bash
   ./mvnw spring-boot:run
   ```

##  Autenticación

Al iniciar se crea automáticamente un usuario admin:
- **usuario:** `admin`
- **password:** `admin123`

Login:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Usar el token devuelto en los demás endpoints:

```bash
curl http://localhost:8080/api/productos \
  -H "Authorization: Bearer <TOKEN>"
```

##  Endpoints principales

### Productos (con paginación + filtros)

```
GET /api/productos?nombre=lap&precioMin=100&precioMax=2000&categoriaId=1&page=0&size=10&sort=nombre,asc
```

### Crear venta

```bash
POST /api/ventas
Content-Type: application/json

{
  "detalles": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 3, "cantidad": 1 }
  ]
}
```

##  Pruebas

```bash
./mvnw test
```

- `VentaServiceTest`: prueba unitaria de venta exitosa y stock insuficiente.
- `VentaIntegrationTest`: integración con H2, valida rollback completo.

## 🔄 Estrategia de Concurrencia

Usamos **Optimistic Locking** vía `@Version` en la entidad `Producto`.

### ¿Por qué optimistic y no pessimistic?

|                  | Pessimistic (`SELECT ... FOR UPDATE`) | Optimistic (`@Version`) |
|------------------|----------------------------------------|--------------------------|
| Bloqueo          | Sí, bloquea filas                      | No, sin bloqueos        |
| Throughput       | Bajo bajo carga                        | Alto                    |
| Conflictos       | Esperan                                | Fallan rápido y reintentan |
| Uso ideal        | Pocas escrituras concurrentes          | Muchas lecturas, pocos conflictos reales |

Para un sistema de ventas, los conflictos sobre el mismo producto son raros pero posibles (dos cajeros vendiendo el mismo SKU al mismo tiempo). Optimistic permite alto throughput y solo paga el costo del reintento cuando realmente hay conflicto.

### Flujo

1. Transacción T1 lee `Producto(id=1, stock=10, version=5)`.
2. Transacción T2 lee el mismo producto (misma versión).
3. T1 hace `UPDATE` → version pasa a 6 ✅.
4. T2 hace `UPDATE WHERE version=5` → 0 filas afectadas → `OptimisticLockingFailureException`.
5. Spring lo captura, hace rollback de toda la venta T2.
6. El cliente recibe HTTP 409 con mensaje claro y puede reintentar.

### Atomicidad

Todo el método `registrarVenta` está bajo `@Transactional`. Si falla cualquier producto (stock insuficiente o conflicto de concurrencia), Spring revierte:
- ningún stock se descuenta,
- ninguna venta queda guardada,
- ningún detalle queda colgado.

##  Swagger

Visita `http://localhost:8080/swagger-ui.html` para probar todo desde la UI.  
Pulsa **"Authorize"** y pega el token JWT (sin la palabra `Bearer`).

##  Estructura del proyecto

```
src/main/java/com/rodriguez/inventorysales/
├── config/         # SecurityConfig, OpenApiConfig, JwtAuthenticationFilter
├── controller/     # Endpoints REST
├── dto/            # Request / Response DTOs
├── entity/         # Entidades JPA
├── exception/      # Excepciones custom + GlobalExceptionHandler
├── mapper/         # Conversión Entity ↔ DTO
├── repository/     # Interfaces Spring Data JPA
├── security/       # JwtService, CustomUserDetailsService
├── service/        # Lógica de negocio
└── specification/  # Filtros dinámicos para productos
```

# UniRide 🚗

> Plataforma de carpooling exclusiva para la comunidad de la Pontificia Universidad Javeriana.  
> Conecta conductores y pasajeros de la misma institución para compartir rutas de forma segura, económica y sostenible.

---

## Tabla de contenidos

- [Descripción general](#descripción-general)
- [Arquitectura del sistema](#arquitectura-del-sistema)
- [Stack tecnológico](#stack-tecnológico)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Modelo de datos](#modelo-de-datos)
- [API REST](#api-rest)
- [Seguridad](#seguridad)
- [Configuración y ejecución](#configuración-y-ejecución)
- [Variables de entorno](#variables-de-entorno)
- [Contribuir](#contribuir)
- [Licencia](#licencia)

---

## Descripción general

UniRide es una aplicación web full-stack que permite a estudiantes y docentes de la Javeriana publicar y reservar cupos en viajes compartidos. El sistema gestiona el ciclo de vida completo de un viaje: creación, búsqueda, reserva, confirmación y reseña.

**Características principales**

- Registro con verificación de correo institucional (`@javeriana.edu.co`)
- Publicación de viajes con filtros de seguridad (viaje solo mujeres, aire acondicionado)
- Reserva de cupos con control de disponibilidad en tiempo real
- Sistema de calificación y reseñas entre usuarios
- Panel de control personalizado por rol (PASSENGER / DRIVER / ADMIN)
- Autenticación stateless mediante JWT

---

## Arquitectura del sistema

```
┌─────────────────────────────────────────────────────────┐
│                  Usuario / Navegador                    │
└────────────────────────┬────────────────────────────────┘
                         │ HTTPS
┌────────────────────────▼────────────────────────────────┐
│           Frontend · HTML + Vanilla JS                  │
│  index · dashboard · buscar-viajes · publicar-viaje     │
│              perfil · en-vivo                           │
│          (servido desde /static por Spring Boot)        │
└────────────────────────┬────────────────────────────────┘
                         │ fetch() con Bearer JWT
┌────────────────────────▼────────────────────────────────┐
│         Spring Security — JwtFilter (HMAC-SHA256)       │
│                 Sesión 100 % stateless                  │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│           Backend · Java 17 · Spring Boot 3.3.5         │
│                                                         │
│  AuthController   TripController   BookingController    │
│  ReviewController  UserController  GlobalController     │
│                                                         │
│  AuthService · BookingService · TripService             │
│  Repositories (Spring Data JPA)                         │
└────────────────────────┬────────────────────────────────┘
                         │ JDBC / Hibernate
┌────────────────────────▼────────────────────────────────┐
│         PostgreSQL 16 · Docker · puerto 5432            │
│                                                         │
│   users     trips     bookings     reviews              │
└─────────────────────────────────────────────────────────┘
```

**Sin integraciones de terceros.** Toda la lógica es propia: autenticación, reservas, calificaciones y manejo de roles se implementan internamente sin depender de servicios externos (sin Firebase, sin Stripe, sin servicios de mapas).

---

## Stack tecnológico

| Capa | Tecnología | Versión |
|---|---|---|
| Frontend | HTML5 + CSS3 + Vanilla JS | — |
| Backend | Java + Spring Boot | 17 / 3.3.5 |
| Persistencia ORM | Spring Data JPA (Hibernate) | incluido en Boot |
| Base de datos | PostgreSQL | 16 |
| Autenticación | Spring Security + JJWT | 0.11.5 |
| Cifrado | BCrypt | incluido en Spring Security |
| Validación | Jakarta Bean Validation | incluido en Boot |
| Build tool | Maven Wrapper | — |
| Contenedor DB | Docker / Docker Compose | — |
| Lenguaje adicional | Lombok (reducción de boilerplate) | — |

---

## Estructura del proyecto

```
UniRide/
├── backend/
│   ├── src/main/java/com/uniride/backend/
│   │   ├── BackendApplication.java       # Entry point
│   │   ├── config/
│   │   │   ├── CorsConfig.java           # CORS (todos los orígenes en dev)
│   │   │   └── SecurityConfig.java       # Cadena de filtros JWT
│   │   ├── controller/                   # Endpoints REST
│   │   │   ├── AuthController.java       # /api/auth
│   │   │   ├── TripController.java       # /api/trips
│   │   │   ├── BookingController.java    # /api/bookings
│   │   │   ├── ReviewController.java     # /api/reviews
│   │   │   ├── UserController.java       # /api/users
│   │   │   └── GlobalController.java     # /api/global (público)
│   │   ├── dto/                          # Objetos de transferencia
│   │   ├── exception/                    # GlobalExceptionHandler
│   │   ├── model/                        # Entidades JPA
│   │   │   ├── User.java
│   │   │   ├── Trip.java
│   │   │   ├── Booking.java
│   │   │   ├── Review.java
│   │   │   ├── UserRole.java             # PASSENGER / DRIVER / ADMIN
│   │   │   └── BookingStatus.java        # PENDING / CONFIRMED / CANCELLED
│   │   ├── repository/                   # Interfaces Spring Data JPA
│   │   ├── security/
│   │   │   ├── JwtUtil.java              # Generación y validación de tokens
│   │   │   ├── JwtFilter.java            # Interceptor por petición
│   │   │   └── CustomUserDetailsService.java
│   │   └── service/                      # Lógica de negocio
│   │       ├── AuthService.java
│   │       ├── TripService.java
│   │       └── BookingService.java
│   ├── src/main/resources/
│   │   ├── application.properties        # Configuración datasource y JWT
│   │   └── static/                       # Frontend servido por Spring Boot
│   │       ├── index.html
│   │       ├── dashboard.html
│   │       ├── buscar-viajes.html
│   │       ├── publicar-viaje.html
│   │       ├── en-vivo.html
│   │       ├── perfil.html
│   │       └── css/components.css
│   └── pom.xml
├── database/
│   └── init.sql                          # DDL + datos semilla
├── docker-compose.yml                    # PostgreSQL + volumen persistente
├── Dockerfile                            # Imagen de la aplicación Java
└── .env.example                          # Plantilla de variables de entorno
```

---

## Modelo de datos

### `users`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | BIGSERIAL PK | Identificador único |
| `full_name` | VARCHAR(255) | Nombre completo |
| `email` | VARCHAR(255) UNIQUE | Correo institucional |
| `password_hash` | VARCHAR(255) | Contraseña cifrada con BCrypt |
| `phone` | VARCHAR(20) UNIQUE | Teléfono de contacto |
| `rol` | VARCHAR(20) | `PASSENGER` / `DRIVER` / `ADMIN` |
| `vehicle_plate` | VARCHAR(10) | Placa (solo conductores) |
| `vehicle_color` | VARCHAR(30) | Color del vehículo |
| `rating` | DOUBLE | Calificación promedio (default 5.0) |
| `total_ratings` | INTEGER | Número de reseñas recibidas |
| `faculty` | VARCHAR(100) | Facultad del usuario |
| `created_at` | TIMESTAMP | Fecha de registro |

### `trips`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | BIGSERIAL PK | — |
| `driver_id` | BIGINT FK → users | Conductor del viaje |
| `origin` | VARCHAR(255) | Punto de partida |
| `destination` | VARCHAR(255) | Destino |
| `departure` | TIMESTAMP | Fecha y hora de salida |
| `seats` | INTEGER | Cupos disponibles |
| `price` | DOUBLE | Precio por pasajero (COP) |
| `only_women` | BOOLEAN | Viaje exclusivo para mujeres |
| `has_ac` | BOOLEAN | Vehículo con aire acondicionado |
| `status` | VARCHAR(20) | `ACTIVE` / `FULL` / `CANCELLED` |
| `created_at` | TIMESTAMP | — |

### `bookings`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | BIGSERIAL PK | — |
| `trip_id` | BIGINT FK → trips | Viaje reservado |
| `passenger_id` | BIGINT FK → users | Pasajero que reserva |
| `status` | VARCHAR(50) | `PENDING` / `CONFIRMED` / `CANCELLED` |
| `created_at` | TIMESTAMP | — |

### `reviews`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | BIGSERIAL PK | — |
| `reviewer_id` | BIGINT FK → users | Usuario que califica |
| `reviewed_id` | BIGINT FK → users | Usuario calificado |
| `rating` | INTEGER (1–5) | Puntuación |
| `comment` | TEXT | Comentario opcional |
| `created_at` | TIMESTAMP | — |

---

## API REST

Todos los endpoints protegidos requieren el header:

```
Authorization: Bearer <token>
```

### Autenticación · `/api/auth` (público)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/register` | Registrar nuevo usuario |
| POST | `/api/auth/login` | Iniciar sesión, devuelve JWT |

**Ejemplo de registro:**

```json
POST /api/auth/register
{
  "fullName": "Santiago Gómez",
  "email": "sgomez@javeriana.edu.co",
  "password": "segura123",
  "phone": "3001234567",
  "rol": "DRIVER",
  "faculty": "Ingeniería"
}
```

**Ejemplo de login:**

```json
POST /api/auth/login
{
  "email": "sgomez@javeriana.edu.co",
  "password": "segura123"
}
// Respuesta: { "token": "eyJ...", "email": "...", "rol": "DRIVER" }
```

### Viajes · `/api/trips` (protegido)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/trips/upcoming` | Listar viajes próximos disponibles |
| POST | `/api/trips/search` | Buscar viajes por origen/destino/fecha |
| POST | `/api/trips` | Publicar un nuevo viaje |
| GET | `/api/trips/my-trips` | Viajes publicados por el conductor autenticado |
| GET | `/api/trips/stats` | Estadísticas del usuario (viajes, ingresos) |
| PUT | `/api/trips/{id}/complete` | Marcar viaje como completado |

### Reservas · `/api/bookings` (protegido)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/bookings` | Crear reserva en un viaje |
| GET | `/api/bookings/my-trips` | Reservas del pasajero autenticado |
| GET | `/api/bookings/my-driver-bookings` | Reservas recibidas en mis viajes |
| PUT | `/api/bookings/{id}/confirm` | Confirmar una reserva |
| PUT | `/api/bookings/{id}/complete` | Marcar reserva como completada |
| PUT | `/api/bookings/{id}/reject` | Rechazar una reserva |

### Reseñas · `/api/reviews` (protegido)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/reviews` | Crear reseña a un usuario |
| GET | `/api/reviews/user/{id}` | Obtener reseñas de un usuario |

---

## Seguridad

El sistema implementa autenticación stateless con JWT sobre Spring Security:

1. El cliente envía credenciales a `/api/auth/login`.
2. El servidor valida con `CustomUserDetailsService` y BCrypt.
3. `JwtUtil` genera un token HMAC-SHA256 con expiración de 24 h (configurable).
4. En cada petición posterior, `JwtFilter` extrae y valida el token del header `Authorization`.
5. Las rutas públicas (`/api/auth/**`, `/api/global/**`, archivos estáticos) se configuran explícitamente en `SecurityConfig`.
6. No se usa `HttpSession`: `SessionCreationPolicy.STATELESS`.

> **Nota para producción:** reemplazar el valor de `jwt.secret` en `application.properties` por una clave generada con al menos 256 bits de entropía y administrarla mediante variables de entorno o un gestor de secretos.

---

## Configuración y ejecución

### Requisitos previos

- Java 17+
- Maven 3.8+ (o usar el wrapper incluido `./mvnw`)
- Docker y Docker Compose

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-org/UniRide.git
cd UniRide
```

### 2. Configurar variables de entorno

```bash
cp .env.example .env
# Editar .env con los valores correspondientes
```

### 3. Levantar la base de datos

```bash
docker compose up -d
# PostgreSQL queda disponible en localhost:5432
# El script database/init.sql se ejecuta automáticamente
```

### 4. Ejecutar el backend

```bash
cd backend
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.  
El frontend se sirve directamente desde `http://localhost:8080/index.html`.

### Usando Docker para todo el stack

```bash
# Construir imagen de la aplicación
docker build -t uniride-app .

# Ejecutar junto con la DB
docker compose up
```

### Credenciales de prueba (datos semilla)

| Email | Contraseña | Rol |
|---|---|---|
| `admin@javeriana.edu.co` | `admin123` | ADMIN |
| `maria@javeriana.edu.co` | `admin123` | DRIVER |
| `andrea@javeriana.edu.co` | `admin123` | DRIVER |
| `carlos@javeriana.edu.co` | `admin123` | DRIVER |

---

## Variables de entorno

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://localhost:5432/uniride_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos | `uniride_user` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos | `uniride_pass` |
| `JWT_SECRET` | Clave secreta para firmar tokens | ⚠️ cambiar en producción |
| `JWT_EXPIRATION` | Duración del token en ms | `86400000` (24 h) |

---

## Contribuir

1. Crear una rama desde `main`: `git checkout -b feature/nombre-feature`
2. Hacer commits descriptivos en español o inglés.
3. Abrir un Pull Request con una descripción clara del cambio y capturas de pantalla si aplica.
4. Asegurarse de que `./mvnw test` pase antes de solicitar revisión.

---

## Licencia

Distribuido bajo la licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

*Desarrollado como proyecto académico en la Pontificia Universidad Javeriana · Bogotá, Colombia.*

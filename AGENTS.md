# AGENTS.md — Restaurant Management API

> Compact guide for agents working in this Spring Boot 3.5 / Java 21 / Maven project.

---

## Quick Start

```bash
# Run application (requires PostgreSQL on localhost:5433 or set DB_URL)
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build JAR
./mvnw clean package

# Run single test class
./mvnw test -Dtest=CreateUserUseCaseTest
```

**Required env vars** (defaults in `application.yaml`):
- `DB_URL` — PostgreSQL JDBC URL (default: `jdbc:postgresql://localhost:5433/restaurant-db`)
- `DB_USERNAME` / `DB_PASSWORD` — DB credentials (default: `postgres` / `postgres`)
- `JWT_SECRET` — JWT signing secret (default: `changeMeToAStrongSecret`)
- `JWT_EXPIRATION` — Token TTL in seconds (default: `3600`)
- `JWT_ISSUER` — JWT issuer claim (default: `Restaurant Project API`)

---

## Architecture

**Clean Architecture** organized by **Use Cases** (each action = single class):

```
src/main/java/henrique/igor/restaurantmanagementapi/
├── usecases/
│   ├── auth/          # Login, ActivateAccount, GeneratePasswordRecoveryCode, ChangePassword
│   ├── user/          # CRUD + role hierarchy validation
│   └── dish/          # CRUD + filtering
├── entities/          # JPA entities (User, Dish)
├── repositories/      # Spring Data JPA repositories + Specs
├── mappers/           # MapStruct mappers (generated to target/generated-sources)
├── security/          # JWT, filter, SecurityConfig (stateless, RBAC)
├── rest/
│   ├── controllers/   # Thin controllers delegating to UseCases
│   └── specs/         # OpenAPI/Swagger annotations
├── services/          # Cross-cutting (Email, RandomCode, AuthContext)
├── errors/            # Global exception handler + custom exceptions
└── config/            # Swagger, etc.
```

**Key principle**: Controllers → UseCases → Repositories. No business logic in controllers.

---

## Testing

- **Framework**: JUnit 5 + Mockito
- **Scope**: Unit tests for Use Cases only (mock all dependencies)
- **Run all**: `./mvnw test`
- **Run one**: `./mvnw test -Dtest=ClassName`
- **Test DB**: H2 in-memory (configured in test profile automatically)
- **No integration tests** yet — only unit tests for use cases

---

## Database & Migrations

- **Flyway** migrations in `src/main/resources/db/migration/`
- `V1__create_users_table.sql` — users table (UUID PK, roles, enable flag)
- `V2__create_dishes_table.sql` — dishes table (UUID PK, category, price)
- `V3__add_isEnabled_to_dishes_table.sql` — adds `is_enabled` to dishes
- **Naming**: `V{number}__{description}.sql` — Flyway runs in version order

---

## Security

- **JWT stateless** authentication (`auth0/java-jwt`)
- **Public routes** (no auth):
  - `POST /auth/login`
  - `PATCH /auth/login`, `/auth/generate-password-recovery-code`, `/auth/change-password`, `/auth/activate`
- **Private routes** (require valid JWT): `/users/**`, `/dishes/**`
- **RBAC**: `ADMIN`, `MANAGER`, `WAITER` — enforced in `ValidateRoleHierarchy` (e.g., MANAGER cannot create ADMIN)
- **Swagger/OpenAPI** at `/swagger-ui.html` (public)

---

## Code Generation

- **MapStruct** mappers generate implementations at compile time to `target/generated-sources/annotations/`
- **Lombok** for getters/setters/builders
- Run `./mvnw compile` to regenerate mappers after interface changes

---

## Common Tasks

| Task | Command |
|------|---------|
| Run app | `./mvnw spring-boot:run` |
| Run tests | `./mvnw test` |
| Build | `./mvnw clean package` |
| Regenerate MapStruct | `./mvnw compile` |
| Check Flyway status | `./mvnw flyway:info` |
| Run Flyway migrate | `./mvnw flyway:migrate` |

---

## Gotchas

1. **PostgreSQL required** for `spring-boot:run` — no embedded DB for main profile
2. **MapStruct + Lombok** need `lombok-mapstruct-binding` processor (configured in pom.xml)
3. **Role hierarchy** validated in `CreateUserUseCase` / `UpdateUserUseCase` via `ValidateRoleHierarchy`
4. **EmailService** is mocked in tests; `MockEmailService` exists for dev
5. **JWT secret must be changed** in production (`JWT_SECRET` env var)
6. **No CI/CD pipeline** configured (no `.github/workflows/`)

---

## Project Status

- ✅ Auth & Users (complete with tests)
- 🚧 Dishes CRUD (done, tests pending)
- ⏳ Tables, Orders — not started
- 📦 Docker / CI / Swagger — planned
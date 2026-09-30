# AGENTS.md — Restaurant Management API

> Compact guide for agents working in this Spring Boot 3.5.7 / Java 21 / Maven project.

---

## Quick Start

```bash
# Run application (requires PostgreSQL on localhost:5433 or set DB_URL)
./mvnw spring-boot:run

# Run tests (Runs 110+ tests using H2 in-memory test profile automatically)
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
│   ├── user/          # Create, Update, FindById, List + Role Hierarchy validation
│   ├── dish/          # Create, Update, FindById, List, Filter, Delete
│   │   └── image/     # UploadDishImage, DeleteDishImage, ListDishImages
│   ├── menu/          # Create, Update, FindById, List, Filter, Delete
│   ├── table/         # Create, Update, UpdateStatus, FindById, List, Filter, Delete
│   └── order/         # CreateOrder, AddOrderItem, RemoveOrderItem, UpdateOrderStatus, FindById, Filter
├── entities/          # JPA entities (User, Dish, Menu, MenuDish, RestaurantTable, Order, OrderItem, Image, DishImage)
├── enums/             # UserRole, Category, OrderStatus, TableStatus, TableLocation
├── repositories/      # Spring Data JPA repositories + Specs
├── mapper/            # MapStruct mappers (generated to target/generated-sources)
├── security/          # JWT, filter, SecurityConfig (stateless, RBAC)
├── rest/
│   ├── controllers/   # AuthController, UserController, DishController, MenuController, TableController, DishImageController, OrderController
│   └── specs/         # OpenAPI/Swagger annotations for all controllers
├── services/          # EmailService, RandomCodeService, AuthContextService, ImageStorageService (LocalStorageService)
├── errors/            # Global exception handler + custom exceptions
└── config/            # SwaggerConfig, etc.
```

**Key principle**: Controllers → UseCases → Repositories. No business logic in controllers.

---

## Testing

- **Framework**: JUnit 5 + Mockito + H2 Database
- **Scope**: Unit tests for Use Cases + Context tests using `@ActiveProfiles("test")`
- **Run all**: `./mvnw test`
- **Run one**: `./mvnw test -Dtest=ClassName`
- **Test Profile**: `src/test/resources/application-test.yaml` configures H2 in-memory DB in PostgreSQL compatibility mode.

---

## Database & Migrations

- **Flyway** migrations in `src/main/resources/db/migration/`
- `V1__create_users_table.sql` — users table (UUID PK, roles, enable flag)
- `V2__create_dishes_table.sql` — dishes table (UUID PK, category, price)
- `V3__add_isEnabled_to_dishes_table.sql` — adds `is_enabled` to dishes
- `V4__create_menu_and_menu_dishes_tables.sql` — menu and menu_dishes tables
- `V5__create_missing_tables.sql` — restaurant_table, image, dish_images, restaurant_order, order_item tables
- **Naming**: `V{number}__{description}.sql` — Flyway runs in version order

---

## Security & Documentation

- **JWT stateless** authentication (`auth0/java-jwt`)
- **Public routes** (no auth):
  - `POST /auth/login`
  - `PATCH /auth/login`, `/auth/generate-password-recovery-code`, `/auth/change-password`, `/auth/activate`
  - `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`
- **Private routes** (require valid JWT): `/users/**`, `/dishes/**`, `/menus/**`, `/tables/**`, `/orders/**`
- **RBAC**: `ADMIN`, `MANAGER`, `WAITER` — enforced in `ValidateRoleHierarchy` and `@PreAuthorize`
- **Swagger/OpenAPI UI** accessible at `/swagger-ui.html` when app is running.

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

## Project Status

- ✅ Auth & Users (Complete with UseCases, Controllers, Specs, Tests)
- ✅ Dishes & Dish Images (Complete with UseCases, Controllers, Specs, Storage, Tests)
- ✅ Menus (Complete with UseCases, Controllers, Specs, Tests)
- ✅ Tables (Complete with UseCases, Controllers, Specs, Tests)
- ✅ Orders & Order Items (Complete with UseCases, Controllers, Specs, Tests)
- ✅ CI/CD & Test Profile (GitHub Actions workflow + H2 test configuration)
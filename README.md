# 🍽️ Restaurant Management API

API REST robusta e completa desenvolvida com **Spring Boot 3.5**, **Java 21** e **Clean Architecture**. O objetivo do projeto é desacoplar integralmente as regras de negócio de frameworks e bibliotecas, garantindo uma aplicação testável, sustentável, altamente documentada e de fácil manutenção.

![Build & Test Pipeline](https://github.com/igorhgds/restaurant-management-api/actions/workflows/ci.yml/badge.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger/OpenAPI 3](https://img.shields.io/badge/Swagger/OpenAPI_3-85EA2D?style=for-the-badge&logo=openapi-initiative&logoColor=black)

---

## 🚀 Tecnologias & Ferramentas

* **Linguagem & Framework:** Java 21, Spring Boot 3.5.7 (Spring Web, Spring Security, Spring Data JPA)
* **Banco de Dados & Migrações:** PostgreSQL, H2 Database (para testes isolados), Flyway (Versionamento do schema V1 a V5)
* **Segurança:** Spring Security com autenticação **JWT (Stateless)** e controle de acesso baseado em papéis (**RBAC**: `ADMIN`, `MANAGER`, `WAITER`)
* **Mapeamento & Utilitários:** MapStruct, Lombok, Jackson
* **Documentação Interativa:** SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui.html`)
* **Testes & CI/CD:** JUnit 5, Mockito, H2 Test Profile (110+ testes unitários/contexto), GitHub Actions Workflow

---

## 🧠 Engenharia e Arquitetura

A estrutura do projeto segue rigorosamente os princípios de **S.O.L.I.D** e **Clean Architecture**, priorizando a organização por casos de uso (`usecases`):

```
src/main/java/henrique/igor/restaurantmanagementapi/
├── usecases/
│   ├── auth/          # Login, Ativação de Conta, Troca de Senha, Código de Recuperação
│   ├── user/          # CRUD de Usuários + Validação de Hierarquia de Roles
│   ├── dish/          # CRUD de Pratos + Filtros JPA Specifications
│   │   └── image/     # Upload, Remoção e Listagem de Imagens de Pratos
│   ├── menu/          # CRUD de Cardápios + Associação de Pratos
│   ├── table/         # CRUD de Mesas + Gestão de Status e Capacidade
│   └── order/         # Gestão de Pedidos, Itens, Transição de Status e Vínculo com Mesas
├── entities/          # Entidades JPA (User, Dish, Menu, RestaurantTable, Order, OrderItem, Image)
├── enums/             # UserRole, Category, OrderStatus, TableStatus, TableLocation
├── repositories/      # Spring Data JPA Repositories + Specifications
├── mapper/            # MapStruct Mappers (gerados em tempo de compilação)
├── security/          # Filtros JWT, SecurityConfig stateless e AuthContextService
├── rest/
│   ├── controllers/   # Controllers REST enxutos delegando aos UseCases
│   └── specs/         # Interfaces OpenAPI/Swagger com anotações de documentação
├── services/          # Serviços transversais (EmailService, RandomCode, ImageStorageService)
└── errors/            # ApiExceptionHandler global e exceções personalizadas
```

---

## 🗺️ Módulos & Funcionalidades (100% Concluído)

### ✅ Autenticação & Usuários
- [x] Login e geração de Token JWT
- [x] Cadastro de usuários com validação de hierarquia de papéis (`ADMIN` > `MANAGER` > `WAITER`)
- [x] Ativação de conta por código enviado por e-mail
- [x] Geração de código e fluxo de recuperação/troca de senha

### ✅ Gestão de Pratos (Dishes) & Imagens
- [x] CRUD de pratos com categorias, preços e especificações JPA
- [x] `DishImageController` e serviço de armazenamento `ImageStorageService` (upload e exclusão de imagens)

### ✅ Gestão de Cardápios (Menus)
- [x] CRUD de cardápios com vínculo de pratos, datas de início/fim e filtros de ativos
- [x] Endpoints expostos via `MenuController` e documentados no Swagger

### ✅ Gestão de Mesas (RestaurantTable)
- [x] Cadastro e listagem de mesas por localização (`INDOOR`, `OUTDOOR`, `BALCONY`, `VIP`) e capacidade
- [x] Atualização de status da mesa (`AVAILABLE`, `OCCUPIED`, `RESERVED`, `OUT_OF_SERVICE`)

### ✅ Fluxo de Pedidos & Itens (Orders & OrderItems)
- [x] Abertura de pedidos vinculados à mesa e garçom (alteração automática da mesa para `OCCUPIED`)
- [x] Adição e remoção dinâmica de itens no pedido com cálculo de subtotais e valor total
- [x] Máquina de estados de pedido (`OPEN` -> `PREPARING` -> `READY` -> `DELIVERED` -> `CLOSED` / `CANCELLED`)
- [x] Liberação automática da mesa para `AVAILABLE` ao fechar ou cancelar o pedido

### ✅ Infraestrutura, Qualidade & CI/CD
- [x] Documentação Swagger/OpenAPI 3 disponível em `/swagger-ui.html`
- [x] Suíte de testes unitários com H2 em memória (execução do `./mvnw test` sem depender de banco externo)
- [x] Pipeline automatizado de CI no GitHub Actions (`.github/workflows/ci.yml`)

---

## 🛠️ Como Executar

### 1. Executando a Aplicação Localmente (PostgreSQL via Docker)

```bash
# 1. Clone o repositório
$ git clone https://github.com/igorhgds/restaurant-management-api.git
$ cd restaurant-management-api

# 2. Inicie o banco de dados PostgreSQL
$ docker compose up -d

# 3. Execute a aplicação Spring Boot
$ ./mvnw spring-boot:run
```

Acesse o Swagger UI em: `http://localhost:8080/swagger-ui.html`

### 2. Executando a Suíte de Testes (H2 em Memória)

```bash
# Executar todos os testes automatizados (110+ testes)
$ ./mvnw test
```

---

Desenvolvido por **Igor Henrique Gomes**

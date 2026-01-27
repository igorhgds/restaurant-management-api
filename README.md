# 🍽️ Restaurant Management API

Nota: Este projeto foi desenvolvido para fins de estudo durante a minha preparação para o mercado. Atualmente, pausei o desenvolvimento para focar na minha atuação profissional como FullStack na Sankhya. O código permanece aqui como histórico do meu aprendizado.

![Status do Projeto](https://img.shields.io/badge/Status-Arquivado_(Foco_em_Carreira)-lightgrey)

> 🚧 **Projeto em Construção** - Foco atual: Expansão das regras de negócio (Pedidos e Mesas).

API REST robusta desenvolvida com foco em **Clean Architecture**. O objetivo é desacoplar as regras de negócio de frameworks e bibliotecas, garantindo um código testável, sustentável e de fácil manutenção.

## 🚀 Tecnologias & Ferramentas

<div style="display: inline_block">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" />
</div>

## 🧠 Engenharia e Arquitetura

A estrutura do projeto segue princípios de **S.O.L.I.D** e **Clean Architecture**, priorizando a organização por casos de uso (`usecases`):

* **Use Cases Pattern:** Cada ação do sistema (ex: `CreateUser`, `Login`, `ActivateAccount`) é uma classe isolada com responsabilidade única.
* **Segurança:** Implementação de Spring Security com autenticação via **JWT (Stateless)**. Rotas protegidas (RBAC).
* **Tratamento de Exceções:** Global Exception Handler para padronização de respostas de erro.
* **Testes Automatizados:** Testes unitários focados na camada de Use Cases para garantir a integridade das regras de negócio.

## 🗺️ Roadmap e Status

### ✅ Módulo de Autenticação & Usuários (Concluído)
- [x] Login e Geração de Token JWT
- [x] Criação de Usuário (Requer Autenticação)
- [x] Ativação de Conta
- [x] Recuperação de Senha (Geração de Código)
- [x] Troca de Senha
- [x] Testes Unitários dos Use Cases (Login, CreateUser)

### 🚧 Módulo de Restaurante (Em Desenvolvimento)
- [x] CRUD de Pratos e Categorias
- [ ] Gestão de Mesas
- [ ] Fluxo de Pedidos e Status

### 🔮 Infraestrutura e DevOps (Futuro)
- [ ] Swagger/OpenAPI para documentação da API
- [ ] Containerização completa (App + DB)
- [ ] Pipeline de CI/CD

## 🛠️ Como executar

```bash
# Clone o repositório
$ git clone [https://github.com/igorhgds/restaurant-management-api.git](https://github.com/igorhgds/restaurant-management-api.git)

# Entre na pasta
$ cd restaurant-management-api

# Execute com Maven
$ mvn spring-boot:run
```
Desenvolvido por Igor Henrique Gomes

---

### 📐 Fluxo de Autenticação (Exemplo)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant LoginUseCase
    participant Repository
    
    Client->>Controller: POST /auth/login
    Controller->>LoginUseCase: execute(credentials)
    LoginUseCase->>Repository: findByEmail(email)
    Repository-->>LoginUseCase: UserDetails
    LoginUseCase->>LoginUseCase: validatePassword()
    LoginUseCase->>Controller: JWT Token
    Controller-->>Client: 200 OK (Token)

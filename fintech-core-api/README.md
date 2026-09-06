# Fintech FIAP 2026 — Core API (Spring Boot)

## 📌 Visão Geral
Backend da plataforma Fintech FIAP 2026 reescrito em **Java 17/21 + Spring Boot 3**, disponibilizando API REST compatível com o dashboard React (`ui-dashboard`), persistência em banco de dados em memória **H2** com Spring Data JPA, autenticação via Bearer Token validada no Supabase e motor de projeção orçamentária (Budget Velocity) com integração ao AWS Bedrock.

---

## 🚀 Endpoints REST

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| `GET` | `/api` | Pública | Mensagem da API e versão (`0.1.0`) |
| `GET` | `/api/health` | Pública | Status da API e conectividade com Supabase Auth |
| `GET` | `/api/me` | Bearer Token | Retorna ID e e-mail do usuário autenticado |
| `GET` | `/api/agent/budget-insight` | Bearer Token | Projeção orçamentária, cálculo de desvio, threat level e ações sugeridas |

---

## 🛠️ Tecnologias Utilizadas
- **Java 17 / 21**
- **Spring Boot 3.3.x**
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-validation`
  - `spring-boot-starter-actuator`
- **H2 Database** (em memória para execução local imediata sem necessidade de instalação de banco)
- **AWS SDK for Java v2 (Bedrock Runtime)**
- **JUnit 5 + MockMvc + Mockito** para testes unitários e de integração
- **Jackson Datatype JSR310** para manipulação padronizada de datas/horas e formato JSON em snake_case

---

## 💻 Como Executar

### 1. Pré-requisitos
- JDK 17 ou 21
- Maven 3.8+ (ou utilize seu IDE preferido: IntelliJ IDEA, VS Code, Eclipse)

### 2. Executando Localmente com Maven
```bash
cd fintech-core-api
mvn spring-boot:run
```

A API estará acessível em `http://localhost:8080`.

### 3. Console do H2 Database
Com a aplicação em execução, o console do banco H2 pode ser acessado em:
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:fintechdb`
- **User:** `sa`
- **Password:** *(em branco)*

### 4. Executando Testes Automatizados
```bash
mvn clean test
```

### 5. Executando via Docker
```bash
docker build -t fintech-core-api:latest .
docker run -p 8080:8080 fintech-core-api:latest
```

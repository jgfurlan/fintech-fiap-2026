# Atomic Feature Spec: [JGF-25] Spring Boot Backend Migration

## 1. Isolated Goal
Provide a production-grade 1:1 Java Spring Boot 3 rewrite of the FastAPI `core-api/` backend inside `fintech-core-api/`. The service exposes identical HTTP endpoints (`GET /api`, `GET /api/health`, `GET /api/me`, `GET /api/agent/budget-insight`), handles Supabase Bearer token verification, runs pure-math and agentic budget velocity analysis with deterministic test seeding, integrates an H2 in-memory JPA store, and includes a multi-stage Dockerfile and updated Docker Compose service.

## 2. Design Decisions
- **Project Structure:** Standalone Maven project in `fintech-core-api/` using Spring Boot 3.3.x / Java 17/21.
- **Data & Persistence Layer:** H2 Database (in-memory) with Spring Data JPA. Preloads seed profiles (`Transaction`, `BudgetSetting`, `UserProfile`) on application startup.
- **Auth Layer:** Bearer token authentication filter/interceptor verifying tokens with Supabase Auth API (`GET /auth/v1/user`), matching FastAPI `get_user` dependency semantics (returning HTTP 401 with `{"detail": "..."}` on failure).
- **Core Endpoints:**
  - `GET /api` -> `RootResponse` (`{"message": "Fintech FIAP 2026 Core API", "version": "0.1.0"}`)
  - `GET /api/health` -> `HealthResponse` (`{"status": "ok", "supabase": {"reachable": bool, "error": str|null, "url_configured": bool}}`)
  - `GET /api/me` -> `UserResponse` (`{"id": "...", "email": "..."}`)
  - `GET /api/agent/budget-insight` -> `BudgetInsightResponse` (velocity projection, threat level, deviation, action items)
- **Agentic & Velocity Logic:**
  - `FintechBudgetVelocityWorker`: Linear projection based on current day and total days in month; calculates category deviation and threat levels (`good`, `warning`, `critical`, `overrun`).
  - `FintechBedrockInsightService`: AWS Bedrock client (Claude 3 Sonnet) with fallback heuristic generator when Bedrock is disabled or unreachable.
  - `FintechAgentCoordinator`: Coordinates profile retrieval, velocity calculation, and enrichment with action items.
- **Compatibility & Frontend Invariants:**
  - CORS enabled for all origins, methods, headers (`*`).
  - Error response body uses `{"detail": "..."}` format to preserve compatibility with `ui-dashboard` frontend fetch error handlers.
  - Default server port: `8080`.
- **Packaging & Containerization:** Multi-stage `Dockerfile` with Maven build stage and lightweight Alpine JRE runtime stage.

## 3. Step-by-Step Implementation Map
- [ ] Create `fintech-core-api/pom.xml` with dependencies: `spring-boot-starter-web`, `spring-boot-starter-validation`, `spring-boot-starter-data-jpa`, `h2`, `jackson`.
- [ ] Create Maven Wrapper (`mvnw`, `.mvn/wrapper/maven-wrapper.properties`).
- [ ] Create `application.properties` / `application.yml` with port 8080, H2 configuration, Supabase configs.
- [ ] Create DTO and Model classes in `br.com.fiap.fintech.dto` / `br.com.fiap.fintech.model`:
  - `RootResponse`, `HealthResponse`, `SupabaseHealth`, `UserResponse`, `BudgetInsightResponse`, `ActionItem`, `ThreatLevel`
- [ ] Create JPA Entities in `br.com.fiap.fintech.entity`:
  - `TransactionEntity`, `BudgetSettingEntity`, `UserProfileEntity`
- [ ] Implement JPA Repositories in `br.com.fiap.fintech.repository`.
- [ ] Implement `FintechSeedDataLoader` CommandLineRunner to seed default test user (`seed-user-001`, Ellie Chen) matching FastAPI `seed_transactions.py`.
- [ ] Implement `FintechAuthService` / `SupabaseClient` for `/auth/v1/health` and `/auth/v1/user` checks.
- [ ] Implement `FintechBudgetVelocityWorker` for budget calculation.
- [ ] Implement `FintechBedrockService` for Bedrock / fallback action items.
- [ ] Implement `FintechAgentCoordinator` orchestrating the worker and Bedrock services.
- [ ] Implement REST Controllers:
  - `FintechCoreApiController`: `/api`, `/api/health`, `/api/me`
  - `FintechAgentController`: `/api/agent/budget-insight`
- [ ] Implement `FintechGlobalExceptionHandler` returning `{"detail": "..."}`.
- [ ] Implement CORS configuration filter.
- [ ] Implement Unit & Integration Tests with `@WebMvcTest` / `@SpringBootTest` (covering all 4 endpoints, auth failures, budget calculation).
- [ ] Create multi-stage `fintech-core-api/Dockerfile`.
- [ ] Update `docker-compose.yml` to include or switch `core-api` to `fintech-core-api`.
- [ ] Update `docs/guidelines/progress-tracker.md` to document Phase 2 initiation and JGF-25 status.

## 4. Verification Array
- [ ] `pom.xml` is valid and all Java source files compile without syntax errors.
- [ ] `GET /api` returns message and version 0.1.0.
- [ ] `GET /api/health` checks Supabase and returns structured health status.
- [ ] `GET /api/me` without Authorization header returns 401 with `{"detail": "Missing Authorization header"}`.
- [ ] `GET /api/me` with invalid token returns 401 with `{"detail": "Invalid token..."}`.
- [ ] `GET /api/me` with valid mock returns 200 with id and email.
- [ ] `GET /api/agent/budget-insight` returns 200 with complete `BudgetInsightResponse` for `seed-user-001`.
- [ ] Unit & integration tests pass with 100% success on all core routes.

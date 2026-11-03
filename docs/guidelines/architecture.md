# Architecture: Systemic Boundaries & Invariants

## Tech Stack Lock-in (FIAP College Stack Mandate)
- **Core Backend:** Java (17 or 21) + Spring Boot 3.3.x in `fintech-core-api/`.
  - Spring Data JPA, Spring Security / JWT auth, Bean Validation.
- **Database:** Oracle SQL Database (FIAP Phase 6 Physical Model) in `docs/database/oracle-phase-6/`.
  - Core tables: `T_USUARIO`, `T_CONTA`, `T_CATEGORIA`, `T_TRANSACAO`, `T_INVESTIMENTO`, `T_META`.
  - Sequences: `sq_usuario`, `sq_conta`, `sq_categoria`, `sq_transacao`, `sq_investimento`, `sq_meta`.
  - Local/CI testing uses H2 with Oracle dialect compatibility (`MODE=Oracle`).
- **Frontend:** React 18+ (Vite, TypeScript, Tailwind CSS) in `ui-dashboard/`.
- **AI/ML Microservice:** Python 3.12+ (FastAPI) for predictive analytics, budget velocity, and agentic LLM orchestration (AWS Bedrock Claude 3 / heuristic fallback).
- **Communication:** RESTful JSON APIs, MCP (Model Context Protocol) for agent tool access.
- **Deployment:** Docker & AWS (ECS Fargate / App Runner).

## Agentic Orchestration (CWD Framework)
To prevent "Global Dementia," all agentic features must follow the **Coordinator-Worker-Delegator** pattern:
1. **Coordinator (LLM / Agent Service):** Clarifies user intent, synthesizes context, and plans execution trajectories.
2. **Worker (Tool / Core API):** Executes atomic, verifiable database and financial actions (e.g., `GET /api/transactions`, ledger mutations).
3. **Delegator (AI Microservice):** Runs predictive models, anomaly scoring, and budget velocity analysis.

## Systemic Invariants (Do Not Break)
- **Account-Bound Security Context:** `T_TRANSACAO` and `T_INVESTIMENTO` have no direct `id_usuario`; ownership is resolved strictly through `id_conta -> T_CONTA -> id_usuario`. All queries and mutations must enforce this hierarchy to prevent tenant data leaks.
- **Ledger Invariants:** Transaction values (`vl_transacao`) must be strictly positive (`> 0`) with directional classification via `tp_transacao` (`'R'` for credit/receita, `'D'` for debit/despesa).
- **Sequence Integrity:** Primary keys are generated using designated Oracle sequences (`sq_*`).
- **Stateless Agent Operations:** Every agent tool call must be treated as an isolated, idempotent, and verifiable request.
- **Unified DTO / Entity Separation:** Core API must maintain strict separation between JPA entities (`br.com.fiap.fintech.entity`) and API contracts/DTOs (`br.com.fiap.fintech.dto`).

## Verification Loops (RLVR)
All system alterations require an automated verification array:
- **Core Backend:** Maven test suite (`./mvnw test`) must pass with 100% success on JPA entity mapping and REST endpoints.
- **AI Service:** `pytest` suite must pass for analytical algorithms and fallback heuristics.
- **Frontend:** TypeScript compilation and Vite build (`npm run build`) must produce zero errors.
- **Verifiable Reward:** Every task must return a numerical signal (+1.0 for success, -0.5 for failure).

## Schema & Query Source of Truth
- `docs/database/oracle-phase-6/01-modelo-ddl.sql`: Authoritative Oracle physical DDL and constraints.
- `docs/database/oracle-phase-6/02-comandos.sql`: Authoritative Oracle SQL query patterns (CRUD, balance checks, filters).
- `docs/specs/JGF-25-college-stack-and-phase6-sync.md`: Architectural specification for the college stack lock-in.
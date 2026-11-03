# Atomic Feature Spec: [JGF-25] College Stack Mandate & Phase 6 Oracle SQL Sync

## 1. Context & Isolated Goal
Align the entire Fintech FIAP 2026 platform architecture with the official FIAP college curriculum requirements (Phase 6 Physical Database & Java Enterprise Application) while maintaining modern software engineering excellence:
- **Core Backend:** Java 17/21 + Spring Boot 3.x enterprise backend.
- **Relational Database:** Oracle SQL Database utilizing the official Phase 6 relational model (`T_USUARIO`, `T_CONTA`, `T_CATEGORIA`, `T_TRANSACAO`, `T_INVESTIMENTO`, `T_META`).
- **Modern Frontend:** React + Vite (TypeScript, Tailwind CSS) replacing legacy Create React App.
- **AI/ML Service:** Python microservice dedicated to predictive analytics, budget velocity, and agentic LLM orchestration (AWS Bedrock / local fallback).

---

## 2. College Stack Mandate & Architectural Invariants

### 2.1 Stack Components
| Layer | Technology | Repository Location | Purpose |
|---|---|---|---|
| **Core Backend** | Java 17/21, Spring Boot 3.3.x, Spring Data JPA, Spring Security / JWT | `fintech-core-api/` | Primary business logic, transactional integrity, RESTful APIs, Oracle persistence |
| **Database** | Oracle SQL Database (Oracle XE / Free 23c / Cloud DB) | `docs/database/oracle-phase-6/` | Source of truth relational schema, constraints, sequences, and reporting queries |
| **Frontend** | React 18+, Vite, TypeScript, Tailwind CSS / Shadcn UI | `ui-dashboard/` | Responsive financial dashboard, transaction feeds, goal tracking, agent chat |
| **AI/ML Microservice** | Python 3.12+, FastAPI / Uvicorn, scikit-learn / statsmodels | `agent-service/` (or `core-api/`) | Predictive financial analytics, anomaly detection, autonomous agent reasoning |

### 2.2 Phase 6 Oracle Relational Model
The database layer adheres strictly to `docs/database/oracle-phase-6/01-modelo-ddl.sql` and `02-comandos.sql`:

1. **`T_USUARIO`**: User identity and credentials (`id_usuario`, `nm_usuario`, `ds_email`, `ds_senha`, `dt_cadastro`). Primary key `pk_usuario`, sequence `sq_usuario`, unique constraint `uk_usuario_email`.
2. **`T_CONTA`**: User accounts/wallets (`id_conta`, `id_usuario`, `tp_conta`, `vl_saldo_atual`). Foreign key `fk_conta_usuario` -> `T_USUARIO(id_usuario)`, sequence `sq_conta`.
3. **`T_CATEGORIA`**: Categorization for transactions (`id_categoria`, `nm_categoria`, `tp_categoria`). Check constraint `ck_categoria_tipo` (`'R'` for receita, `'D'` for despesa), sequence `sq_categoria`.
4. **`T_TRANSACAO`**: Unified ledger for income and expense (`id_transacao`, `id_conta`, `id_categoria`, `vl_transacao`, `dt_transacao`, `ds_transacao`, `tp_transacao`).
   - Check `ck_transacao_tipo` (`tp_transacao IN ('R','D')`).
   - Check `ck_transacao_valor` (`vl_transacao > 0`).
   - Foreign keys `fk_transacao_conta` -> `T_CONTA`, `fk_transacao_categ` -> `T_CATEGORIA`.
   - Sequence `sq_transacao`.
5. **`T_INVESTIMENTO`**: Financial assets & investments (`id_investimento`, `id_conta`, `tp_investimento`, `vl_aplicado`, `dt_aplicacao`, `tx_rendimento`).
   - Foreign key `fk_investimento_conta` -> `T_CONTA`.
   - Sequence `sq_investimento`.
6. **`T_META`**: Savings and financial goals (`id_meta`, `id_usuario`, `ds_meta`, `vl_objetivo`, `dt_limite`, `vl_atual`).
   - Foreign key `fk_meta_usuario` -> `T_USUARIO`.
   - Sequence `sq_meta`.

### 2.3 System Invariants
1. **Account-Bound Security Context:** Transactions and investments do not carry direct `id_usuario` keys. Ownership is strictly established through `id_conta -> T_CONTA -> id_usuario`. Any query or mutation must validate user ownership via join/subquery to avoid cross-tenant data leakage.
2. **Deterministic Sequence Allocation:** Primary keys are generated using designated Oracle sequences (`sq_*`).
3. **Signed Ledger Discipline:** `vl_transacao` is always positive with directional sign dictated by `tp_transacao` (`R` = credit, `D` = debit).
4. **Spring Data JPA Alignment:** Java entities in `fintech-core-api` map 1:1 with Oracle tables and columns using standard `@Table(name = "t_...")`, `@Column(name = "...")`, and `@SequenceGenerator(name = "sq_...")`.
5. **Decoupled AI Engine:** The Java backend remains lean and transactional, delegating heavy forecasting and LLM agent orchestration to the Python AI service via clean REST/JSON contracts.

---

## 3. Implementation Roadmap & Steps

### Step 1: Oracle Database Version Control & Reference Sync
- [x] Copy Phase 6 DDL (`01-modelo-ddl.sql`) and sample queries (`02-comandos.sql`) to `docs/database/oracle-phase-6/`.
- [ ] Maintain an Oracle Docker Compose profile (`gvenzl/oracle-free:23-slim` or `container-registry.oracle.com/database/free:latest`) for local integration testing.
- [ ] Configure Spring Boot profile `oracle` with Oracle JDBC Driver (`com.oracle.database.jdbc:ojdbc11`) and Hibernate Oracle dialect.

### Step 2: Spring Boot JPA Entity & Repository Synchronization
- [ ] Define `@Entity` classes in `fintech-core-api/src/main/java/br/com/fiap/fintech/entity/`:
  - `UsuarioEntity` (`t_usuario`, `sq_usuario`)
  - `ContaEntity` (`t_conta`, `sq_conta`)
  - `CategoriaEntity` (`t_categoria`, `sq_categoria`)
  - `TransacaoEntity` (`t_transacao`, `sq_transacao`)
  - `InvestimentoEntity` (`t_investimento`, `sq_investimento`)
  - `MetaEntity` (`t_meta`, `sq_meta`)
- [ ] Create Spring Data JPA repositories with query methods mirroring `02-comandos.sql` (e.g., balance updates, transactional history by user account, monthly expense aggregations).

### Step 3: Frontend Evolution (React + Vite)
- [ ] Migrate `ui-dashboard/` build toolchain from Create React App (CRA) to Vite for instant HMR and optimized production bundles.
- [ ] Standardize API client against Spring Boot endpoints (`http://localhost:8080/api/...`).
- [ ] Ensure WCAG 2.2 AA accessibility and theme consistency.

### Step 4: Python AI/ML Microservice Integration
- [ ] Retain and focus Python service for data science:
  - Predictive velocity models (budget depletion, anomaly detection).
  - AWS Bedrock Claude 3 / LLM synthesis for natural language coaching.
- [ ] Expose endpoint consumed by Java Spring Boot `FintechAgentCoordinator` or directly by frontend via API gateway.

---

## 4. Verification Array & RLVR Metrics

- [ ] **SQL Invariant Check:** All tables, sequences, constraints, and check conditions in `docs/database/oracle-phase-6/01-modelo-ddl.sql` are mirrored without semantic drift in JPA entities.
- [ ] **Entity Persistence Test:** Automated test suite runs against in-memory H2 (Oracle mode `MODE=Oracle;DEFAULT_NULL_NAME=NULL`) and verifies schema generation matching Oracle DDL.
- [ ] **Account Boundary Check:** Multi-user isolation test confirms a user cannot query or mutate transactions or investments belonging to another user's account.
- [ ] **Vite Build Verification:** Frontend builds cleanly with zero TypeScript errors (`npm run build`).
- [ ] **Cross-Service Health:** End-to-end check verifies Core API (Java), Database (Oracle/H2), and AI service (Python) communicate within SLA (< 200ms latency for synchronous queries).

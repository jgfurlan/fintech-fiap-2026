# Atomic Feature Spec: [JGF-23] Budget Velocity Insight Agent

## 1. Isolated Goal
Transform the static "Anticipatory Banking" widget into a data-driven component powered by a Budget Velocity insight. The agent reasons about the user's transactional seed data, projects month-end totals against budget settings, and returns explainable actions to the surface.

## 2. Design Decisions
- **Architecture:** Reactive path (FastAPI endpoint) → Worker (pure math) → Frontend (widget)
- **Data Strategy:** Seeded transaction data + seeded budget settings for Phase 1 Tracer Bullet (no Supabase reads, deterministic tests)
- **Extensibility:** Worker takes `(transactions, budget_settings)` as plain data — swapping seeds for real queries is a point change
- **Naming Convention:** All symbols prefixed `fintech_worker_*` / `fintech_agent_coordinator_*` per agent-legibility rules

## 3. Step-by-Step Implementation Map
- [ ] Create `app/data/seed_transactions.py` — per-user realistic transaction datasets + budget constants
- [ ] Create `app/models/seed.py` — Pydantic models: `Transaction`, `BudgetSetting`, `SeedUserProfile`
- [ ] Implement `app/workers/budget_velocity_worker.py` — `fintech_worker_budget_velocity_insight(transactions, budget_settings)` using strictly typed data classes; returns `BudgetInsightResponse`
- [ ] Implement `app/coordinator.py` — `fintech_agent_coordinator_analyze_budget(user_id)` fetches seed profile, calls worker, returns insight
- [ ] Create `app/routers/agent.py` — `GET /api/agent/budget-insight` (Depends `get_user`), wires coordinator
- [ ] Wire router into `main.py`
- [ ] Update frontend — make widget call endpoint, replace hardcoded strings with live data
- [ ] Write tests — worker unit tests, coordinator integration, API endpoint integration, all using `AsyncClient` + `dependency_overrides`

## 4. Verification Array
- [ ] `GET /api/agent/budget-insight` with valid token returns `BudgetInsightResponse`
- [ ] Worker returns stable/identical results for identical seed data
- [ ] Widget displays projected deviation, threat level, and action items
- [ ] `ruff check .` → All checks passed
- [ ] `ruff format --check .` → All files formatted
- [ ] `pytest -v` → All existing + new tests pass (no regressions)

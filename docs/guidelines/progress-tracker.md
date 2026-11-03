# Progress Tracker: Dynamic State Anchor

## Current Project Phase
**Phase 2: College Stack Lock-in & Enterprise Architecture**

## Active Implementation
- **Active Issue:** `[JGF-25]` College Stack Mandate & Phase 6 Oracle SQL Sync
- **Current Task:** Document college stack mandate, lock in Oracle Phase 6 schema, and align roadmap guidelines.
- **Status:** In Review / Specification Complete

## Completed Features
- ✅ Repository initialized and workflow rules established.
- ✅ Phase 6 Oracle SQL physical schema (`01-modelo-ddl.sql`) and queries (`02-comandos.sql`) synced to `docs/database/oracle-phase-6/`.
- ✅ Atomic Feature Spec created: `docs/specs/JGF-25-college-stack-and-phase6-sync.md`.
- ✅ Architecture guidelines (`docs/guidelines/architecture.md`) updated with college stack lock-in.

## Historical Decisions
- **2026-09-29:** College Stack Mandate finalized:
  - Core Backend: Java 17/21 + Spring Boot 3.3.x (`fintech-core-api/`).
  - Database: Oracle SQL Database using Phase 6 physical model (`T_USUARIO`, `T_CONTA`, `T_CATEGORIA`, `T_TRANSACAO`, `T_INVESTIMENTO`, `T_META`).
  - Frontend: React + Vite + TypeScript (`ui-dashboard/`).
  - AI/ML Service: Python 3.12+ (FastAPI) for predictive analytics and agentic reasoning.
- **2026-05-15:** Adopted Agent-Led Workflow (The Router Pattern) and Linear/ClickUp issue traceability.

## Feature Queue & Roadmap
1. `[JGF-25]` Spring Boot JPA entity and repository implementation aligned with Phase 6 Oracle schema (`fintech-core-api`).
2. `[JGF-26]` Frontend toolchain migration from CRA to Vite (`ui-dashboard`).
3. `[JGF-27]` Python AI/ML microservice integration for predictive budget velocity and Bedrock insights.
4. `[JGF-28]` End-to-end integration and Docker Compose environment (Oracle DB + Spring Boot + Vite + Python AI).

## Session Restoration Point
When starting a fresh session (`/clear`), the agent reads this file to immediately resume the active task and branch without re-reading past conversation transcripts.

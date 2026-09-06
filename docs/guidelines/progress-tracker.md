# Progress Tracker: Dynamic State Anchor

## Current Project Phase
**Phase 1: Tracer Bullet** (May – Jun 2026)
*Goal: Validate the FastAPI/React/Supabase pipeline end-to-end.*

## Active Implementation
- **Current Task:** AWS Migration Pivot: App Runner → ECS Fargate.
- **Status:** Documentation updated; CI/CD pipeline rewrite in progress.

## Completed Features
- ✅ FastAPI Backend Skeleton (core-api).
- ✅ React Frontend Skeleton (ui-dashboard).
- ✅ Supabase Integration (Auth/DB).
- ✅ Linear MCP Configuration (Gemini & Pi Agent).
- ✅ Graphify Knowledge Graph Setup.
- ✅ Research Integration (NotebookLM, Agentic AI papers).
- ✅ [Arch] Link SupabaseHealth model to health() endpoint (JGF-5).
- ✅ [Test] Mock get_user for /api/me success path (JGF-6) — spec: `docs/specs/JGF-6-mock-get-user.md`.
- ✅ [UX] Implement Anticipatory Banking patterns (JGF-9).
- ✅ [Security] Unit tests for get_user Auth Dependency (JGF-8).
- ✅ [API] Add /api/transactions and /api/balance endpoints (JGF-20).
- ✅ [Auth] Protect /api/transactions and /api/balance with get_user dependency (JGF-21).
- ✅ [A11y] WCAG 2.2 AA compliance + mobile responsiveness (JGF-22).
- ✅ [Showcase] Strategic Migration to AWS (Bedrock Integration) (JGF-23).
- ✅ [Backend] Spring Boot 3 Backend 1:1 Parity with H2 JPA (JGF-25) — spec: `docs/specs/JGF-25-spring-boot-backend.md`.

## Historical Decisions
- **2026-05-14:** Keep CRA for Phase 1; defer Next.js to Phase 4 (JGF-7 Canceled).
- **2026-05-15:** Adopt Ghost AI "Spec-Driven Development" (Six-File Context Matrix).
- **2026-05-15:** Move all agent skills to global directories for multi-agent support.
- **2026-05-15:** Mandatory Linear-GitHub synchronization enforced.
- **2026-05-15:** Pi Agent completed JGF-5, JGF-20, and JGF-21.
- **2026-05-19:** Strategic migration to AWS Bedrock completed (JGF-23).
- **2026-05-22:** **Pivot:** Switching from AWS App Runner to **AWS ECS Fargate** for enhanced scalability and showcase complexity.
- **2026-09-06:** **Phase 2 Migration:** Port core backend from FastAPI to Java Spring Boot 3 (`fintech-core-api`), with H2 JPA store, deterministic seeding, Supabase Bearer Auth, and multi-stage Dockerfile (JGF-25).

## Linear Milestones
- **M1: Tracer Bullet Baseline** (Target: 2026-06-01) — *Status: In Progress*
- **M1.5: AWS Showcase Migration** (Target: 2026-05-25) — *Status: Implementing ECS*
- **M2: Core Domain Implementation** (Target: 2026-07-01)
- **M2.5: Spring Boot Backend Parity** (Target: 2026-09-10) — *Status: Complete*

## Project Updates
- **2026-05-22:** Initiated pivot to AWS ECS Fargate. High-level architecture updated in `architecture.md`.
- **2026-09-06:** Implemented `fintech-core-api` Spring Boot project with 1:1 route compatibility for `/api`, `/api/health`, `/api/me`, `/api/agent/budget-insight`.

## Feature Queue (Linear Issues)
1. `[Showcase] Migrate to AWS ECS Fargate` (JGF-24)
2. `[Backend] Spring Boot Backend Migration` (JGF-25)
3. `[Security] Implementation of Rate Limiting` (Proposed)

## Session Restoration Point
Agents resuming work should start by querying the `graphify` knowledge graph for the latest `Active Implementation` state.
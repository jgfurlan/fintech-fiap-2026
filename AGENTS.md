# fintech-fiap-2026 Agent Instructions

This project is a fintech full-stack monorepo deployed to Google Cloud Run.

## Phase 1 — Tracer Bullet (May-Jun)
Validate pipeline end-to-end with a fast stack:
- **Frontend:** Next.js (TypeScript) in `ui-dashboard/`
- **Backend:** FastAPI (Python) in `core-api/`
- **Auth/DB:** Supabase
- **Styling:** Rose-Pine theme + Tailwind CSS
- **Accessibility:** WCAG 2.2 AA
- **Deploy:** Docker → Google Artifact Registry → Google Cloud Run
- **CI/CD:** GitHub Actions

## Phase 2 — Migration to Java (Jul-Aug)
Rewrite `core-api/` → `core-spring/` (Spring Boot, Java).
Business logic stays same; goal is learning Java + Spring idioms.

## Phase 3 — AI Agent (Sep-Oct)
Add Python microservice for LangGraph-based agent features.
Communicates with Spring Boot backend via HTTP/event bus.

## Phase 4 — Polish & Deploy (Nov)

## Directory Structure
```
.
├── .github/workflows/   # CI/CD
├── core-api/            # FastAPI (Phase 1 backend)
├── core-spring/         # Spring Boot (Phase 2, future)
└── ui-dashboard/        # Next.js frontend
```

## Development
- Run locally: `docker compose up`
- After code changes, run `graphify update .` to keep knowledge graph current

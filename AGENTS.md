# fintech-fiap-2026 Agent Instructions

This project is a fintech full-stack monorepo deployed to Google Cloud Run.

## Phase 1 — Tracer Bullet (May-Jun)
Validate pipeline end-to-end with a fast stack:
- **Frontend:** React (TypeScript, CRA) in `ui-dashboard/` — CRA is sufficient for Phase 1 (SSR not needed, single-page auth dashboard); Next.js deferred to Phase 4 if at all
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
└── ui-dashboard/        # React frontend
```

## Development
- Run locally: `docker compose up`
- After code changes, run `graphify update .` to keep knowledge graph current

## graphify (Knowledge Graph)

This project has a graphify knowledge graph at `graphify-out/`.

**Mandatory session resumption rules to save tokens:**
- **Read First:** Before reading any code or running grep, read `graphify-out/GRAPH_REPORT.md` to understand the architecture, "god nodes," and community structure.
- **Navigate Wiki:** If `graphify-out/wiki/index.md` exists, navigate the wiki instead of reading raw files one-by-one.
- **Query Graph:** For cross-module questions ("how does X relate to Y"), prefer `graphify query "<question>"`, `graphify path "<A>" "<B>"`, or `graphify explain "<concept>"` over grep. These tools traverse the graph's extracted and inferred edges.
- **Maintain Graph:** After modifying any code or documentation files in this session, run `graphify update .` to keep the graph current. This ensures the next session starts with an accurate map.

## Multi-Session Plan
See `SESSIONS.md` for the detailed session-by-session plan.

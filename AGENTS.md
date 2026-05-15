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

## Git Workflow

**One feature = one branch = one PR.** No exceptions.

1. **Create branch from `main`**: `git checkout -b <type>/<short-name> main`
2. **Commit & push**: small, atomic commits with conventional commit messages
3. **Open PR against `main`**: wait for CI checks before merging
4. **Squash-merge** into `main` after approval

### Branch naming conventions

| Prefix | Purpose | Example |
|--------|---------|---------|
| `feat/` | New feature or enhancement | `feat/transaction-api` |
| `fix/` | Bug fix | `fix/uvicorn-module-path` |
| `ci/` | CI/CD pipeline changes | `ci/add-pr-checks` |
| `docs/` | Documentation only | `docs/session1-cleanup` |
| `refactor/` | Code restructuring, no behavior change | `refactor/extract-auth-middleware` |
| `chore/` | Maintenance (deps, config) | `chore/update-deps` |

### Rules

- **Never commit unrelated work to an existing branch.** If you're on `feat/A` and need to fix a bug, create a separate `fix/B` branch from `main`.
- **Never force-push shared branches** unless correcting a mistake (like we did cleaning `ci/add-pr-checks`).
- **Keep PRs small and focused.** If a branch grows past 2-3 concerns, split it.
- **Always run tests before pushing**: `cd core-api && pytest`
- **Update Linear issues** when a PR closes them (move to Done, add comment with PR link)
- **Update `graphify`** after merging to main: `graphify update .`

### Linear Integration

- **No MCP server available.** Use the Linear GraphQL API directly with the key in `.env`.
- After creating/merging a PR, update the corresponding Linear issue state.
- When creating new issues, assign to project `Fintech FIAP 2026` and team `JGF`.
- Never commit API keys — use `${LINEAR_API_KEY}` in config files, actual key only in `.env` (gitignored).

## Development

- Run locally: `docker compose up`
- Run backend tests: `cd core-api && pytest`
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

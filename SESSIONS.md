# Multi-Session Development Plan

> Each session has a single focus, clear acceptance criteria, and produces a deployable increment.

---

## Phase 1: Tracer Bullet (May–Jun 2026)

### Session 1: Documentation & Health 🔧
**Status**: In Progress | **Goal**: Fix all docs & understand where we are
- ✅ Update `README.md` with real setup instructions
- ✅ Update `issue-001.md` to match current stack (FastAPI, not Spring Boot)
- ✅ Write `DESIGN.md` with actual Rose Pine specs
- ⚖️ **Open Question**: Investigate `React (CRA) → Next.js` CMS vs. `keep CRA` in AGENTS.md

**Acceptance**:
- [ ] `README.md` is accurate and a new dev can get running in <5 min
- [ ] All references to Spring Boot in Phase 1 docs corrected
- [ ] `DESIGN.md` is no longer an empty Figma placeholder
- [ ] `graphify update .` run and graph reflects current state

**Output**: `docs/` is trustworthy, graph updated.

---

### Session 2: Next.js Frontend Migration (or Decision) 🔄
**Goal**: Resolve the CRA / Next.js mismatch definitively

**Path A — Migrate to Next.js**:
- Port `src/App.tsx` logic to `app/page.tsx`
- Keep Tailwind, Supabase client, and tests
- Update Docker build and env var handling (`NEXT_` prefix)
- Verify Cloud Run deploy still works

**Path B — Keep CRA, Update AGENTS.md**:
- Document why CRA is sufficient for Phase 1
- Update `AGENTS.md` to reflect actual stack
- Close the discrepancy

**Acceptance**:
- [ ] Decision documented in `decisions/` or issue comment
- [ ] Frontend deploys successfully after change (or explicitly kept as-is)
- [ ] All existing tests pass

**Output**: One consistent frontend framework, no more mismatch.

---

### Session 3: Fintech Domain — Core Transaction API 💰
**Goal**: Stop being an auth demo; become a real fintech app

- Add Pydantic models: `Transaction`, `Wallet`, `Balance`
- Create `/api/transactions` endpoint (list user's transactions)
- Create `/api/balance` endpoint (current wallet balance)
- Add Supabase table schemas (or document them)
- Update tests for new endpoints

**Acceptance**:
- [ ] Authenticated user can `GET /api/transactions`
- [ ] Authenticated user can `GET /api/balance`
- [ ] Both endpoints return Pydantic-schematized JSON
- [ ] pytest covers happy path and auth failure

**Output**: Backend has real domain logic.

---

### Session 4: Fintech Domain — Dashboard UI 💳
**Goal**: Display transaction and balance data

- Add transaction list view to dashboard
- Add balance card / summary widget
- Handle empty state, loading state, and error state
- Keep Rose-Pine theme, ensure accessible markup

**Acceptance**:
- [ ] Logged-in user sees their balance on the dashboard
- [ ] Logged-in user sees a styled transaction list
- [ ] Empty state is handled gracefully ("No transactions yet")
- [ ] Loading skeleton or spinner
- [ ] No unhandled exceptions

**Output**: Frontend looks like a fintech product.

---

### Session 5: Accessibility & Mobile Polishing ♿
**Goal**: Meet WCAG 2.2 AA and basic mobile responsiveness

- Run automated a11y checks (axe, Lighthouse)
- Add `aria-label` to interactive elements
- Ensure keyboard navigation works for auth flow
- Fix color contrast issues (if any)
- Add responsive breakpoints for mobile view

**Acceptance**:
- [ ] Lighthouse Accessibility score ≥ 90
- [ ] App is usable on mobile (tested in dev tools)
- [ ] Keyboard-only navigation works for sign-in / sign-out
- [ ] Screen reader announces status changes (API health, auth state)

**Output**: Polished frontend, a11y documented.

---

### Session 6: Backend Hardening & Observability 🔒
**Goal**: Production-grade safety before moving to Phase 2

- Add centralized error handling middleware (not raw 500s)
- Add request logging (structlog or similar)
- Add input validation (Pydantic already helps, but tighten up)
- Add basic rate limiting (SlowAPI or middleware)
- Review and handle all `TODO`, `FIXME`, or `X-` comments

**Acceptance**:
- [ ] All 4xx/5xx responses have consistent JSON error shape
- [ ] Request logs are emitted per call
- [ ] Rate limiting active on auth endpoints
- [ ] No unhandled exceptions leak stack traces in prod

**Output**: Backend is production-hardened for Phase 1 scope.

---

### Session 7: Test Coverage & DX Polish 🧪
**Goal**: Raise confidence before declaring Phase 1 done

- Frontend: Add test for auth flow, transaction list
- Backend: Add integration tests for transactions
- CI: Ensure tests run on PR, not just `main`
- Add pre-commit hooks (lint + format)
- Add `Makefile` or `Taskfile` for common commands

**Acceptance**:
- [ ] Backend coverage > 70%
- [ ] Frontend has component tests for at least 3 components
- [ ] PR template and branch protection configured
- [ ] Lint/format runs in CI

**Output**: High confidence in every deploy.

---

## Phase 2: Migration to Java (Jul–Aug 2026)
Planned. Will create dedicated issue/ADR when Phase 1 is officially closed.

## Phase 3: AI Agent (Sep–Oct 2026)
Planned. Will create dedicated issue/ADR when Phase 2 is underway.

## Phase 4: Polish & Production Deploy (Nov 2026)
Planned. Final security audit, performance optimization, and monitoring.

---

## Current Session Tracker

| # | Title | Status | Date |
|---|-------|--------|------|
| 1 | Documentation & Health | In Progress | 2026-05-13 |
| 2 | Next.js Migration | Open | TBD |
| 3 | Transaction API | Open | TBD |
| 4 | Dashboard UI | Open | TBD |
| 5 | Accessibility & Mobile | Open | TBD |
| 6 | Backend Hardening | Open | TBD |
| 7 | Test Coverage & DX | Open | TBD |

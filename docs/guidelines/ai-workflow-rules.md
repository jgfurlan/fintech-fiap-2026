# AI Workflow Rules: Execution Governance & Tiered Precision

## Core Mandates
All agents must focus exclusively on a singular system boundary or isolated subsystem task at a time. Work is grounded in **Karpathy's Engineering Principles**: Think Before Coding, Simplicity First, Surgical Changes, and Goal-Driven Execution.

---

## 1. Tiered Governance Model
To maximize precision while eliminating token waste, apply the governance tier that matches the task risk:

| Tier | Task Type & Scope | Required Spec & Planning | Verification Ceremony |
| :--- | :--- | :--- | :--- |
| **Tier 1 (Trivial)** | Typos, lint fixes, single config tweaks, <20 LOC changes. | No spec PR. State goal directly in prompt / commit. | Direct TDD / unit test + quiet linter check. |
| **Tier 2 (Standard)** | Normal features, standard API endpoints, isolated bug fixes. | Single lightweight spec or plan approved before code. | TDD (RED $\to$ GREEN) + Verification gate. |
| **Tier 3 (Architectural)** | Schema changes, auth flows, breaking changes, core protocols. | Formal Two-File Spec in `docs/specs/ID-<issue>/` (`product.md` + `tech.md`). | Full TDD suite + Mutation/negative test + Integration verification. |

---

## 2. Invariant-Grounded TDD (Goal-Driven Execution)
Every code change must be mathematically grounded in testable invariants:

1. **RED (Failing Proof):**
   - Write a unit or integration test matching the target invariant *before* modifying production code.
   - Run the test suite and confirm it **fails** for the expected reason on current code.
2. **GREEN (Surgical Implementation):**
   - Write the *minimal* code to pass the failing test (no speculative abstractions or unasked features).
   - Re-run the test and confirm it passes.
3. **REFACTOR & SIMPLIFY:**
   - Review the diff against Karpathy's Simplicity First rule ("If 200 lines could be 50, rewrite it").
   - Ensure no adjacent files or unrelated formatting were touched.

---

## 3. Deterministic Verification Gate
Before declaring any task complete or opening a PR, the agent MUST run deterministic verification commands:
- **Test Suite:** Execute in quiet mode (e.g. `npm test -- --silent` or `pytest -q`).
- **Linter & Types:** Execute quiet checks (e.g. `ruff check --quiet`, `tsc --noEmit`).
- **Zero Hallucination Proof:** The agent must see real output confirming 0 errors before reporting success.

---

## 4. Session Reset & State Anchoring Protocol
To prevent context window degradation and token explosion:
1. **Scope Cap:** 1 Session = 1 ClickUp Task / Branch. Keep conversation length under 25 turns.
2. **Anchor State:** Upon reaching a milestone or completing a task, update `docs/guidelines/progress-tracker.md`.
3. **Reset:** Instruct the user or use `/clear` between tasks. A fresh session starts with a 100% focused attention window.

---

## 5. ClickUp-GitHub Synchronization (Mandatory)
ClickUp is the "Source of Truth" for project state. GitHub is the "Source of Truth" for code.

1. **Task Mapping:** Every GitHub branch MUST correspond to exactly one ClickUp task (`<task-id>-<short-description>`).
2. **Commit Messages:** Include the ClickUp task ID: `feat: [<task-id>] <action>` or `fix: [<task-id>] <action>`.
3. **State Transitions:**
   - Work starts: Move ClickUp task to **"In Progress"**.
   - PR opened: Move ClickUp task to **"In Review"** and attach PR URL.
   - Merged: Move ClickUp task to **"Done"**.

---

## 6. Prohibited Actions
- **No Unsolicited Refactoring:** Never modify adjacent code, comments, or formatting outside the target scope.
- **No Speculative Abstraction:** Never create helper modules or configurability for single-use logic.
- **No Silent Failures:** Never use `pass` or bare generic catch-alls.
- **No Multi-tasking:** Never attempt to fix unrelated bugs while implementing an active task.

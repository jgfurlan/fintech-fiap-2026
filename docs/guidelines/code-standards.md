# Code Standards: Agent-Legibility & Compliance

## Agent-Legibility (Mandatory)
The LLM is the primary consumer of this codebase. Optimize for searchability and deterministic discovery.

1. **Global Uniqueness:** Prefix function names to avoid search ambiguity.
   - Format: `{module}_{action}` (e.g., `core_api_calculate_health_score`).
2. **Explicit Error Paths:** No bare catch-alls. Every `try...except` must catch a specific exception and return a meaningful error.
3. **No "Magic" Strings:** Use constants or Enums for status values and configuration keys.

## Type Compliance
- **Python:** Strict Pydantic models for all API boundaries. Use type hints for all function signatures.
- **TypeScript:** `any` is strictly prohibited. Use interfaces for all props and state.

## Linting & Formatting
- **Python:** `ruff` for linting and formatting.
- **Frontend:** `eslint` and `prettier` (default CRA config).

## Testing Standards (TDD)
- **RED:** Write a failing test that reproduces the requirement/bug.
- **GREEN:** Implement the minimal code to pass.
- **REFACTOR:** Clean up while maintaining GREEN.
- *Verification Requirement:* No PR can be merged without new tests covering the change.
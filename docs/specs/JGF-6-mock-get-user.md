# Atomic Feature Spec: [JGF-6] Mock get_user for /api/me success path

## 1. Isolated Goal
Implement and verify unit + integration tests for the `/api/me` endpoint's success path by mocking the `get_user` dependency, ensuring authenticated users receive a valid `UserResponse` without requiring a live Supabase connection.

## 2. Design Decisions
- **Unit tests** (`tests/test_auth.py`): Call `get_user()` directly with `pytest-httpx` mocking the Supabase HTTP call
- **Integration tests** (`tests/test_main.py`): Use `app.dependency_overrides[get_user]` to inject a mock user dict at the FastAPI router level
- **Fixture:** `pytest-httpx` `httpx_mock` for unit-level HTTP interception; `autouse` `monkeypatch` for env vars
- **Cleanup:** `finally: app.dependency_overrides.clear()` prevents cross-test state leakage
- **Schema:** Validate against `UserResponse(id: str, email: EmailStr)` Pydantic model

## 3. Step-by-Step Implementation Map
- [x] Add `pytest-httpx>=0.30.0` to `requirements.txt`
- [x] Create `tests/test_auth.py` with unit-level `get_user` tests:
  - [x] `test_fintech_core_api_get_user_missing_header` — raises 401
  - [x] `test_fintech_core_api_get_user_invalid_token` — httpx_mock returns 401
  - [x] `test_fintech_core_api_get_user_valid_token` — httpx_mock returns 200 + user JSON
- [x] Update `tests/test_main.py` with integration-level mock via `dependency_overrides`:
  - [x] `test_me_endpoint_success` — override returns `{"id", "email"}`, assert 200
  - [x] `test_me_endpoint_invalid_token` — override raises `HTTPException(401)`
- [x] Fix ruff lint: remove unused `BaseModel` import in `main.py`, move model import to top
- [x] Fix ruff format: apply `ruff format` across all core-api files

## 4. Verification Array
- [x] `test_fintech_core_api_get_user_missing_header` — 401, "Missing Authorization header"
- [x] `test_fintech_core_api_get_user_invalid_token` — 401, "Invalid token" in detail
- [x] `test_fintech_core_api_get_user_valid_token` — returns `{"id": ..., "email": ...}`
- [x] `test_me_endpoint_success` — 200, `{"email": "test@example.com", "id": "test_uuid"}`
- [x] `test_me_endpoint_invalid_token` — 401, "Invalid token" in detail
- [x] Full `pytest -v` → 8/8 passed
- [x] `ruff check .` → All checks passed
- [x] `ruff format --check .` → All files formatted

## 5. Git Workflow Certification
| Check | Status | Detail |
|-------|--------|--------|
| Branch created | ✅ | `test/JGF-6-mock-get-user` |
| Branch naming | ⚠️ | `test/` prefix — deviates from strict `<issue-id>-<short-desc>` but consistent with project convention (`feat/`, `bug/`, `ci/`) |
| Commit message | ✅ | `test: [JGF-6] mock get_user for /api/me success path` — Linear ID present |
| Merged to main | ✅ | Merge commit `740c88a` |
| Dependency added | ✅ | `pytest-httpx>=0.30.0` in `requirements.txt` |
| Lint clean | ✅ | Post-fix: ruff check + format pass |

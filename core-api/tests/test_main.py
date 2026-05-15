import pytest
from httpx import AsyncClient, ASGITransport
from fastapi import HTTPException
from app.main import app, get_user


@pytest.mark.asyncio
async def test_health_endpoint():
    async with AsyncClient(
        transport=ASGITransport(app=app), base_url="http://test"
    ) as ac:
        response = await ac.get("/api/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "ok"
    assert "supabase" in data
    assert "reachable" in data["supabase"]


@pytest.mark.asyncio
async def test_root_endpoint():
    async with AsyncClient(
        transport=ASGITransport(app=app), base_url="http://test"
    ) as ac:
        response = await ac.get("/api")
    assert response.status_code == 200
    data = response.json()
    assert data["message"] == "Fintech FIAP 2026 Core API"
    assert "version" in data


@pytest.mark.asyncio
async def test_me_endpoint_no_auth():
    async with AsyncClient(
        transport=ASGITransport(app=app), base_url="http://test"
    ) as ac:
        response = await ac.get("/api/me")
    assert response.status_code == 401
    assert response.json() == {"detail": "Missing Authorization header"}


@pytest.mark.asyncio
async def test_me_endpoint_invalid_token():
    async def override_get_user_invalid():
        raise HTTPException(status_code=401, detail="Invalid token")

    app.dependency_overrides[get_user] = override_get_user_invalid
    try:
        async with AsyncClient(
            transport=ASGITransport(app=app), base_url="http://test"
        ) as ac:
            response = await ac.get(
                "/api/me", headers={"Authorization": "Bearer invalid_token"}
            )
        assert response.status_code == 401
        assert "Invalid token" in response.json()["detail"]
    finally:
        app.dependency_overrides.clear()


@pytest.mark.asyncio
async def test_me_endpoint_success():
    async def override_get_user():
        return {"id": "test_uuid", "email": "test@example.com"}

    app.dependency_overrides[get_user] = override_get_user
    try:
        async with AsyncClient(
            transport=ASGITransport(app=app), base_url="http://test"
        ) as ac:
            response = await ac.get(
                "/api/me", headers={"Authorization": "Bearer valid_token"}
            )
        assert response.status_code == 200
        assert response.json() == {"email": "test@example.com", "id": "test_uuid"}
    finally:
        app.dependency_overrides.clear()

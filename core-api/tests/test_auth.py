import pytest
from fastapi import HTTPException
import app.main
from app.main import get_user
import httpx
import os

@pytest.fixture(autouse=True)
def setup_env(monkeypatch):
    monkeypatch.setattr(app.main, "SUPABASE_URL", "https://test.supabase.co")
    monkeypatch.setattr(app.main, "SUPABASE_ANON_KEY", "test_key")

@pytest.mark.asyncio
async def test_fintech_core_api_get_user_missing_header():
    """Verify that get_user raises 401 when Authorization header is missing."""
    with pytest.raises(HTTPException) as excinfo:
        await get_user(authorization=None)
    assert excinfo.value.status_code == 401
    assert excinfo.value.detail == "Missing Authorization header"

@pytest.mark.asyncio
async def test_fintech_core_api_get_user_invalid_token(httpx_mock):
    """Verify that get_user raises 401 when Supabase returns 401."""
    httpx_mock.add_response(status_code=401)
    
    with pytest.raises(HTTPException) as excinfo:
        await get_user(authorization="Bearer invalid")
    assert excinfo.value.status_code == 401
    assert "Invalid token" in excinfo.value.detail

@pytest.mark.asyncio
async def test_fintech_core_api_get_user_valid_token(httpx_mock):
    """Verify that get_user returns user data when token is valid."""
    mock_user = {"id": "test_uuid", "email": "test@example.com"}
    httpx_mock.add_response(status_code=200, json=mock_user)
    
    result = await get_user(authorization="Bearer valid")
    assert result == mock_user

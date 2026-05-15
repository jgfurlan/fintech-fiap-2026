import pytest
from unittest.mock import patch, AsyncMock, Mock
from httpx import AsyncClient, ASGITransport
from app.main import app


MOCK_USER = {"id": "user-123", "email": "test@example.com", "access_token": "valid_token"}


def override_get_user():
	return MOCK_USER


# --- Existing tests (no dependency override needed) ---


@pytest.mark.asyncio
async def test_health_endpoint():
	async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
		response = await ac.get("/api/health")
	assert response.status_code == 200
	data = response.json()
	assert data["status"] == "ok"
	assert "supabase" in data
	assert "reachable" in data["supabase"]


@pytest.mark.asyncio
async def test_root_endpoint():
	async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
		response = await ac.get("/api")
	assert response.status_code == 200
	data = response.json()
	assert data["message"] == "Fintech FIAP 2026 Core API"
	assert "version" in data


@pytest.mark.asyncio
async def test_me_endpoint_no_auth():
	async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
		response = await ac.get("/api/me")
	assert response.status_code == 401
	assert response.json() == {"detail": "Missing Authorization header"}


@pytest.mark.asyncio
async def test_me_endpoint_invalid_token():
	async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
		response = await ac.get("/api/me", headers={"Authorization": "Bearer invalid_token"})
	assert response.status_code == 401
	assert "Invalid token" in response.json()["detail"]


# --- Session 3: Transaction & Balance endpoints ---

MOCK_TRANSACTIONS = [
	{
		"id": "tx-1",
		"amount": 150.00,
		"currency": "BRL",
		"description": "Grocery store",
		"category": "food",
		"created_at": "2026-05-10T10:00:00Z",
	},
	{
		"id": "tx-2",
		"amount": -50.00,
		"currency": "BRL",
		"description": "Transfer out",
		"category": "transfer",
		"created_at": "2026-05-11T14:30:00Z",
	},
]

MOCK_WALLET = [{
	"id": "wallet-user-123",
	"available": 1250.50,
	"currency": "BRL",
}]


@pytest.mark.asyncio
async def test_transactions_no_auth():
	"""Unauthenticated request to /api/transactions returns 401."""
	async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
		response = await ac.get("/api/transactions")
	assert response.status_code == 401
	assert response.json() == {"detail": "Missing Authorization header"}


@pytest.mark.asyncio
async def test_balance_no_auth():
	"""Unauthenticated request to /api/balance returns 401."""
	async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
		response = await ac.get("/api/balance")
	assert response.status_code == 401
	assert response.json() == {"detail": "Missing Authorization header"}


@pytest.mark.asyncio
@patch("app.main.httpx.AsyncClient")
async def test_transactions_authenticated(mock_client_cls):
	"""Authenticated GET /api/transactions returns Pydantic-schematized list."""
	from app.main import get_user

	app.dependency_overrides[get_user] = override_get_user

	mock_resp = AsyncMock()
	mock_resp.status_code = 200
	mock_resp.json = Mock(return_value=MOCK_TRANSACTIONS)

	mock_client = AsyncMock()
	mock_client.__aenter__ = AsyncMock(return_value=mock_client)
	mock_client.__aexit__ = AsyncMock(return_value=False)
	mock_client.get.return_value = mock_resp
	mock_client_cls.return_value = mock_client

	try:
		async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
			response = await ac.get(
				"/api/transactions",
				headers={"Authorization": "Bearer valid_token"},
			)
		assert response.status_code == 200
		data = response.json()
		assert "transactions" in data
		assert len(data["transactions"]) == 2
		assert data["transactions"][0]["id"] == "tx-1"
		assert data["transactions"][0]["amount"] == 150.0
		assert data["transactions"][1]["amount"] == -50.0
	finally:
		app.dependency_overrides.clear()


@pytest.mark.asyncio
@patch("app.main.httpx.AsyncClient")
async def test_balance_authenticated(mock_client_cls):
	"""Authenticated GET /api/balance returns Pydantic-schematized balance."""
	from app.main import get_user

	app.dependency_overrides[get_user] = override_get_user

	mock_resp = AsyncMock()
	mock_resp.status_code = 200
	mock_resp.json = Mock(return_value=MOCK_WALLET)

	mock_client = AsyncMock()
	mock_client.__aenter__ = AsyncMock(return_value=mock_client)
	mock_client.__aexit__ = AsyncMock(return_value=False)
	mock_client.get.return_value = mock_resp
	mock_client_cls.return_value = mock_client

	try:
		async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
			response = await ac.get(
				"/api/balance",
				headers={"Authorization": "Bearer valid_token"},
			)
		assert response.status_code == 200
		data = response.json()
		assert "balance" in data
		assert data["balance"]["wallet_id"] == "wallet-user-123"
		assert data["balance"]["available"] == 1250.5
		assert data["balance"]["currency"] == "BRL"
	finally:
		app.dependency_overrides.clear()

import os
import httpx
from dotenv import load_dotenv
from fastapi import FastAPI, Depends, HTTPException, Header
from fastapi.middleware.cors import CORSMiddleware

from .models.api import (
	HealthResponse,
	RootResponse,
	SupabaseHealth,
	UserResponse,
	Transaction,
	Balance,
	TransactionsResponse,
	BalanceResponse,
)

load_dotenv()

app = FastAPI(title="Fintech FIAP 2026 — Core API")

app.add_middleware(
	CORSMiddleware,
	allow_origins=["*"],
	allow_credentials=True,
	allow_methods=["*"],
	allow_headers=["*"],
)

SUPABASE_URL = os.environ.get("SUPABASE_URL", "http://placeholder")
SUPABASE_ANON_KEY = os.environ.get("SUPABASE_ANON_KEY", "placeholder")


async def get_user(authorization: str = Header(None)):
	if not authorization:
		raise HTTPException(401, "Missing Authorization header")
	token = authorization.removeprefix("Bearer ")
	try:
		async with httpx.AsyncClient() as client:
			resp = await client.get(
				f"{SUPABASE_URL}/auth/v1/user",
				headers={"apikey": SUPABASE_ANON_KEY, "Authorization": f"Bearer {token}"},
				timeout=10.0,
			)
			if resp.status_code != 200:
				raise HTTPException(401, f"Invalid token (Supabase returned {resp.status_code})")
			return resp.json()
	except httpx.HTTPStatusError as e:
		raise HTTPException(401, f"Invalid token: {e.response.text}")
	except httpx.RequestError as e:
		raise HTTPException(401, f"Invalid token: auth service unreachable ({type(e).__name__})")


@app.get("/api/health", response_model=HealthResponse)
async def health():
	supabase_ok = False
	error = None
	try:
		async with httpx.AsyncClient() as client:
			resp = await client.get(
				f"{SUPABASE_URL}/auth/v1/health",
				headers={"apikey": SUPABASE_ANON_KEY},
				timeout=5.0,
			)
			supabase_ok = resp.status_code == 200
	except Exception as e:
		error = str(e)
	return HealthResponse(
		status="ok",
		supabase=SupabaseHealth(
			reachable=supabase_ok,
			error=error,
			url_configured=SUPABASE_URL != "http://placeholder",
		),
	)


@app.get("/api", response_model=RootResponse)
async def root():
	return RootResponse(message="Fintech FIAP 2026 Core API", version="0.1.0")


@app.get("/api/me", response_model=UserResponse)
async def me(user: dict = Depends(get_user)):
	return UserResponse(email=user.get("email"), id=user.get("id"))


@app.get("/api/transactions", response_model=TransactionsResponse)
async def transactions(user: dict = Depends(get_user)):
	"""List transactions for the authenticated user from Supabase."""
	user_id = user.get("id")
	try:
		async with httpx.AsyncClient() as client:
			resp = await client.get(
				f"{SUPABASE_URL}/rest/v1/transactions",
				headers={
					"apikey": SUPABASE_ANON_KEY,
					"Authorization": f"Bearer {user.get('access_token', '')}",
				},
				params={"user_id": f"eq.{user_id}", "order": "created_at.desc", "limit": "50"},
				timeout=10.0,
			)
			if resp.status_code != 200:
				raise HTTPException(502, f"Failed to fetch transactions (Supabase returned {resp.status_code})")
			rows = resp.json()
			txns = [Transaction(**row) for row in rows]
			return TransactionsResponse(transactions=txns)
	except HTTPException:
		raise
	except httpx.RequestError as e:
		raise HTTPException(502, f"Supabase unreachable ({type(e).__name__})")


@app.get("/api/balance", response_model=BalanceResponse)
async def balance(user: dict = Depends(get_user)):
	"""Get wallet balance for the authenticated user from Supabase."""
	user_id = user.get("id")
	try:
		async with httpx.AsyncClient() as client:
			resp = await client.get(
				f"{SUPABASE_URL}/rest/v1/wallets",
				headers={
					"apikey": SUPABASE_ANON_KEY,
					"Authorization": f"Bearer {user.get('access_token', '')}",
				},
				params={"user_id": f"eq.{user_id}", "select": "id,available,currency"},
				timeout=10.0,
			)
			if resp.status_code != 200:
				raise HTTPException(502, f"Failed to fetch balance (Supabase returned {resp.status_code})")
			rows = resp.json()
			if not rows:
				raise HTTPException(404, "No wallet found for this user")
			w = rows[0]
			b = Balance(wallet_id=w["id"], available=w["available"], currency=w.get("currency", "BRL"))
			return BalanceResponse(balance=b)
	except HTTPException:
		raise
	except httpx.RequestError as e:
		raise HTTPException(502, f"Supabase unreachable ({type(e).__name__})")

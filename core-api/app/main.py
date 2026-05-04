from fastapi import FastAPI, Depends, HTTPException, Header
from pydantic import BaseModel
import httpx
import os
from dotenv import load_dotenv

load_dotenv()

app = FastAPI(title="Fintech FIAP 2026 — Core API")

SUPABASE_URL = os.environ["SUPABASE_URL"]
SUPABASE_ANON_KEY = os.environ["SUPABASE_ANON_KEY"]
SUPABASE_REST_URL = f"{SUPABASE_URL}/rest/v1"


async def get_user(authorization: str = Header(None)):
    if not authorization:
        raise HTTPException(401, "Missing Authorization header")
    token = authorization.removeprefix("Bearer ")
    async with httpx.AsyncClient() as client:
        resp = await client.get(
            f"{SUPABASE_URL}/auth/v1/user",
            headers={"apikey": SUPABASE_ANON_KEY, "Authorization": f"Bearer {token}"},
        )
    if resp.status_code != 200:
        raise HTTPException(401, "Invalid token")
    return resp.json()


@app.get("/api/health")
async def health():
    return {"status": "ok"}


@app.get("/api")
async def root():
    return {"message": "Fintech FIAP 2026 Core API", "version": "0.1.0"}


@app.get("/api/me")
async def me(user: dict = Depends(get_user)):
    return {"email": user.get("email"), "id": user.get("id")}
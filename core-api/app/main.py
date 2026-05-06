from fastapi import FastAPI, Depends, HTTPException, Header
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import httpx
import os
from dotenv import load_dotenv

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
SUPABASE_REST_URL = f"{SUPABASE_URL}/rest/v1"


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
        raise HTTPException(502, f"Auth service unreachable: {e}")


from app.models.api import HealthResponse, RootResponse, UserResponse

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
        supabase={
            "reachable": supabase_ok,
            "error": error,
            "url_configured": SUPABASE_URL != "http://placeholder",
        },
    )

@app.get("/api", response_model=RootResponse)
async def root():
    return RootResponse(message="Fintech FIAP 2026 Core API", version="0.1.0")

@app.get("/api/me", response_model=UserResponse)
async def me(user: dict = Depends(get_user)):
    return UserResponse(email=user.get("email"), id=user.get("id"))
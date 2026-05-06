from pydantic import BaseModel, EmailStr
from typing import Optional

class SupabaseHealth(BaseModel):
    reachable: bool
    error: Optional[str] = None
    url_configured: bool

class HealthResponse(BaseModel):
    status: str
    supabase: SupabaseHealth

class RootResponse(BaseModel):
    message: str
    version: str

class UserResponse(BaseModel):
    id: str
    email: EmailStr

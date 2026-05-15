from datetime import datetime
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


class Transaction(BaseModel):
	id: str
	amount: float
	currency: str = "BRL"
	description: str
	category: str = "other"
	created_at: Optional[datetime] = None


class Balance(BaseModel):
	wallet_id: str
	available: float
	currency: str = "BRL"


class TransactionsResponse(BaseModel):
	transactions: list[Transaction]


class BalanceResponse(BaseModel):
	balance: Balance

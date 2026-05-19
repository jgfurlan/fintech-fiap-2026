from fastapi import APIRouter, Depends, status

from app.main import get_user
from app.coordinator import fintech_agent_coordinator_analyze_budget
from app.models.seed import BudgetInsightResponse


router = APIRouter(prefix="/api/agent", tags=["agent"])


@router.get("/budget-insight", response_model=BudgetInsightResponse)
async def fintech_core_api_get_agent_budget_insight(user: dict = Depends(get_user)) -> BudgetInsightResponse:
    """
    Returns a proactive budget-velocity insight for the currently authenticated user.
    Seeded data is used for the Tracer Bullet; swap the coordinator implementation
    for Phase 2 to read from Supabase.
    """
    user_id = user.get("id", "")
    return await fintech_agent_coordinator_analyze_budget(user_id)

"""Coordinator: orchestrates agentic analysis for budget velocity."""

from fastapi import HTTPException, status
from app.models.seed import SeedUserProfile, BudgetInsightResponse
from app.data.seed_transactions import fintech_seed_data_get_user_profile
from app.workers.budget_velocity_worker import fintech_worker_budget_velocity_insight
from app.services.bedrock_service import BedrockInsightService


async def fintech_agent_coordinator_analyze_budget(user_id: str) -> BudgetInsightResponse:
    """
    Orchestrates budget velocity analysis for a given user_id.
    Uses pure-math worker for calculations and AWS Bedrock for agentic insights.
    """
    profile = fintech_seed_data_get_user_profile(user_id)
    if profile is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"No seed profile found for user_id={user_id}",
        )

    if not profile.transactions:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"User {user_id} has no transaction data to analyze.",
        )

    # 1. Perform math analysis via Worker
    insight = fintech_worker_budget_velocity_insight(
        user_id=user_id,
        transactions=profile.transactions,
        budgets=profile.budgets,
    )

    # 2. If there's a deviation, generate agentic suggestions via AWS Bedrock
    if insight.deviation_amount > 0:
        bedrock = BedrockInsightService()
        # Find the category with the worst deviation (simplification for this turn)
        # In a production app, we'd pass more context
        category = "general" # Fallback
        if profile.budgets:
            category = profile.budgets[0].category # Simplified for demo
            
        insight.action_items = bedrock.get_budget_insight(
            category=category,
            deviation=insight.deviation_amount,
            total_spent=insight.projected_month_end_total - insight.deviation_amount
        )

    return insight

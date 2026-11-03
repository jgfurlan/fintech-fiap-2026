from decimal import Decimal
from datetime import date
from unittest.mock import patch
import pytest
from httpx import AsyncClient, ASGITransport
from fastapi import HTTPException

from app.main import app, get_user
from app.models.seed import BudgetSetting, Transaction, ThreatLevel, ActionItem
from app.workers.budget_velocity_worker import (
    fintech_worker_budget_velocity_insight,
    _projected_spend,
    _category_spending_so_far,
)
from app.coordinator import fintech_agent_coordinator_analyze_budget


def test_projected_spend():
    # Spent 100 on day 10 of a 30-day month -> projected 300
    projected = _projected_spend(Decimal("100"), 10, 30)
    assert projected == Decimal("300.0")


def test_category_spending_so_far():
    txs = [
        Transaction(tx_id=1, date=date(2026, 5, 2), amount=Decimal("50.00"), category="dining"),
        Transaction(tx_id=2, date=date(2026, 5, 10), amount=Decimal("100.00"), category="dining"),
        Transaction(tx_id=3, date=date(2026, 5, 25), amount=Decimal("20.00"), category="dining"),
    ]
    # Only sum up to May 10
    spending = _category_spending_so_far(txs, end_date=date(2026, 5, 10))
    assert spending["dining"] == Decimal("150.00")


def test_budget_velocity_worker_overrun():
    # Day 15 of 31-day month: spent 500 on 600 budget -> projected spend > 1000
    txs = [
        Transaction(tx_id=1, date=date(2026, 5, 10), amount=Decimal("500.00"), category="dining"),
    ]
    budgets = [
        BudgetSetting(category="dining", monthly_limit=Decimal("600.00")),
    ]
    insight = fintech_worker_budget_velocity_insight(
        user_id="user-123",
        transactions=txs,
        budgets=budgets,
        today=date(2026, 5, 15),
    )
    assert insight.threat_level == ThreatLevel.OVERRUN
    assert insight.deviation_amount > Decimal("0")
    assert insight.projected_month_end_total > Decimal("600.00")


def test_budget_velocity_worker_within_budget():
    txs = [
        Transaction(tx_id=1, date=date(2026, 5, 10), amount=Decimal("50.00"), category="dining"),
    ]
    budgets = [
        BudgetSetting(category="dining", monthly_limit=Decimal("600.00")),
    ]
    insight = fintech_worker_budget_velocity_insight(
        user_id="user-123",
        transactions=txs,
        budgets=budgets,
        today=date(2026, 5, 15),
    )
    assert insight.threat_level == ThreatLevel.GOOD
    assert insight.deviation_amount <= Decimal("0")


@pytest.mark.asyncio
async def test_coordinator_unknown_user():
    with pytest.raises(HTTPException) as exc:
        await fintech_agent_coordinator_analyze_budget("non-existent-user")
    assert exc.value.status_code == 404


@pytest.mark.asyncio
async def test_coordinator_seed_user_with_mocked_bedrock():
    mock_action = ActionItem(
        action="Cook at home",
        projected_savings=Decimal("100.00"),
        reason="Save on dining",
    )
    with patch("app.coordinator.BedrockInsightService") as MockBedrock:
        instance = MockBedrock.return_value
        instance.get_budget_insight.return_value = [mock_action]

        result = await fintech_agent_coordinator_analyze_budget("seed-user-001")
        assert result.user_id == "seed-user-001"
        assert len(result.action_items) > 0
        assert result.action_items[0].action == "Cook at home"


@pytest.mark.asyncio
async def test_agent_budget_insight_endpoint_unauthorized():
    async with AsyncClient(
        transport=ASGITransport(app=app), base_url="http://test"
    ) as ac:
        response = await ac.get("/api/agent/budget-insight")
    assert response.status_code == 401


@pytest.mark.asyncio
async def test_agent_budget_insight_endpoint_success():
    async def override_get_user():
        return {"id": "seed-user-001", "email": "ellie@chen.dev"}

    mock_action = ActionItem(
        action="Reduce dining outings",
        projected_savings=Decimal("80.00"),
        reason="Dining over budget",
    )
    app.dependency_overrides[get_user] = override_get_user
    try:
        with patch("app.coordinator.BedrockInsightService") as MockBedrock:
            instance = MockBedrock.return_value
            instance.get_budget_insight.return_value = [mock_action]

            async with AsyncClient(
                transport=ASGITransport(app=app), base_url="http://test"
            ) as ac:
                response = await ac.get(
                    "/api/agent/budget-insight",
                    headers={"Authorization": "Bearer fake_token"},
                )
            assert response.status_code == 200
            data = response.json()
            assert data["user_id"] == "seed-user-001"
            assert data["currency"] == "BRL"
            assert "projected_month_end_total" in data
            assert len(data["action_items"]) == 1
            assert data["action_items"][0]["action"] == "Reduce dining outings"
    finally:
        app.dependency_overrides.clear()

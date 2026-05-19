"""Pydantic models for agent data and seed data."""

from datetime import date, datetime
from decimal import Decimal, ROUND_HALF_UP
from enum import StrEnum
from typing import Any

from pydantic import BaseModel, Field, model_validator


class Transaction(BaseModel):
    """A single financial transaction."""

    tx_id: int | None = None
    date: date
    amount: Decimal  # always non-negative for the magnitude
    category: str  # e.g. dining, groceries, entertainment
    description: str | None = None
    user_id: str | None = None


class BudgetSetting(BaseModel):
    """A single budget line-item for a category."""

    category: str
    monthly_limit: Decimal = Field(
        ...,
        description="Budgeted amount for a 30-day window",
        json_schema_extra={"examples": [600.00]},
    )
    alert_threshold: Decimal | None = Field(
        None,
        description="Percentage (0–1) at which a budget alert should fire",
        json_schema_extra={"examples": [0.80]},
    )


class SeedUserProfile(BaseModel):
    """A seeded user's complete data profile for deterministic testing."""

    user_id: str
    display_name: str | None = None
    currency: str = "BRL"
    transactions: list[Transaction] = Field(default_factory=list)
    budgets: list[BudgetSetting] = Field(default_factory=list)

    @model_validator(mode="after")
    def _check_budgets(self) -> "SeedUserProfile":
        """Ensure every budget has a matching transaction category."""
        budget_cats = {b.category for b in self.budgets}
        tx_cats = {t.category for t in self.transactions}
        missing = budget_cats - tx_cats
        if missing:
            raise ValueError(f"Budget categories missing from transactions: {missing}")
        return self


class ThreatLevel(StrEnum):
    """Threat / deviation level for budget projections."""

    GOOD = "good"
    WARNING = "warning"
    CRITICAL = "critical"
    OVERRUN = "overrun"


class ActionItem(BaseModel):
    """A specific, explainable action the user can take."""

    action: str  # e.g. "Skip one restaurant meal this week"
    projected_savings: Decimal
    reason: str


class BudgetInsightResponse(BaseModel):
    """The insight produced by the Budget Velocity Worker."""

    user_id: str
    report_date: date
    currency: str = "BRL"
    projected_month_end_total: Decimal
    budget_total: Decimal
    deviation_pct: Decimal
    deviation_amount: Decimal
    threat_level: str  # ThreatLevel as string for JSON compat
    action_items: list[ActionItem] = Field(default_factory=list)
    generated_at: datetime = Field(default_factory=datetime.utcnow)

    @model_validator(mode="after")
    def _round_monetary(self) -> "BudgetInsightResponse":
        """Round monetary values to two decimals."""
        self.projected_month_end_total = Decimal(str(self.projected_month_end_total)).quantize(
            Decimal("0.01"), rounding=ROUND_HALF_UP
        )
        self.deviation_amount = Decimal(str(self.deviation_amount)).quantize(
            Decimal("0.01"), rounding=ROUND_HALF_UP
        )
        for item in self.action_items:
            item.projected_savings = Decimal(str(item.projected_savings)).quantize(
                Decimal("0.01"), rounding=ROUND_HALF_UP
            )
        return self

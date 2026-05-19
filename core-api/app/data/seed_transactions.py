"""Seeded transaction and budget data for deterministic Tracer Bullet testing."""

from decimal import Decimal
from datetime import date, timedelta
from collections import defaultdict
from statistics import mean, stdev

from app.models.seed import (
    Transaction,
    BudgetSetting,
    SeedUserProfile,
    ThreatLevel,
    BudgetInsightResponse,
    ActionItem,
)


SEED_PROFILES: dict[str, SeedUserProfile] = {}


def _seed_user_001() -> SeedUserProfile:
    """Seed user 001: moderate dining overspend, balanced otherwise."""
    user_id = "seed-user-001"
    tx_list = []  # type: list[Transaction]

    # Simulate transactions for May 2026.
    dining_dates = [1, 2, 4, 5, 7, 9, 11, 12, 14, 15, 18, 20, 22, 23, 25, 26, 28, 29]
    for i, day in enumerate(dining_dates):
        tx = Transaction(
            tx_id=1000 + i,
            date=date(2026, 5, day),
            amount=Decimal(str(85 + (day % 5) * 12)),
            category="dining",
            description="Restaurant",
            user_id=user_id,
        )
        tx_list.append(tx)

    groceries_dates = [3, 10, 17, 24, 31]
    for i, day in enumerate(groceries_dates):
        tx = Transaction(
            tx_id=2000 + i,
            date=date(2026, 5, day),
            amount=Decimal(str(180 + (day % 7) * 15)),
            category="groceries",
            description="Grocery Store",
            user_id=user_id,
        )
        tx_list.append(tx)

    transport_dates = [1, 8, 15, 22, 29]
    for i, day in enumerate(transport_dates):
        tx = Transaction(
            tx_id=3000 + i,
            date=date(2026, 5, day),
            amount=Decimal("60.00"),
            category="transport",
            description="Metro Pass",
            user_id=user_id,
        )
        tx_list.append(tx)

    budgets = [
        BudgetSetting(category="dining", monthly_limit=Decimal("600.00"), alert_threshold=Decimal("0.80")),
        BudgetSetting(category="groceries", monthly_limit=Decimal("800.00"), alert_threshold=Decimal("0.80")),
        BudgetSetting(category="transport", monthly_limit=Decimal("300.00"), alert_threshold=Decimal("0.80")),
    ]

    return SeedUserProfile(
        user_id=user_id,
        display_name="Ellie Chen",
        currency="BRL",
        transactions=tx_list,
        budgets=budgets,
    )


SeedUserProfile.model_rebuild()


def fintech_seed_data_load_profiles() -> dict[str, SeedUserProfile]:
    """Load and return all seed user profiles."""
    global SEED_PROFILES
    if not SEED_PROFILES:
        SEED_PROFILES = {
            "seed-user-001": _seed_user_001(),
        }
    return SEED_PROFILES


def fintech_seed_data_get_user_profile(user_id: str) -> SeedUserProfile | None:
    """Retrieve a single seed user profile by user_id."""
    profiles = fintech_seed_data_load_profiles()
    return profiles.get(user_id)


def fintech_seed_data_all_categories() -> set[str]:
    """Return the union of all transaction categories across all profiles."""
    profiles = fintech_seed_data_load_profiles()
    categories = set()  # type: set[str]
    for profile in profiles.values():
        for tx in profile.transactions:
            categories.add(tx.category)
    return categories

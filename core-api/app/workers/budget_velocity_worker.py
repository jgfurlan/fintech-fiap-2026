"""Budget Velocity Worker: pure math over transactions and budgets."""

from datetime import date, datetime, timedelta
from decimal import Decimal

from app.models.seed import (
    Transaction,
    BudgetSetting,
    BudgetInsightResponse,
    ActionItem,
    ThreatLevel,
)


def _days_in_current_month(today: date) -> int:
    """Return the number of days in the current month."""
    if today.month == 12:
        next_month = date(today.year + 1, 1, 1)
    else:
        next_month = date(today.year, today.month + 1, 1)
    return (next_month - date(today.year, today.month, 1)).days


def _category_spending_so_far(
    transactions: list[Transaction], end_date: date
) -> dict[str, Decimal]:
    """Aggregate spending per category up to (and including) end_date."""
    category: dict[str, list[Decimal]] = {}
    for tx in transactions:
        if tx.date <= end_date:
            category.setdefault(tx.category, []).append(tx.amount)
    return {cat: sum(amounts) for cat, amounts in category.items()}


def _projected_spend(
    current_spent: Decimal,
    current_day: int,
    days_in_month: int,
) -> Decimal:
    """Linear projection: (current_day ) * (days_in_month / current_day) -> current total."""
    if current_day <= 0:
        return current_spent
    rate = int(current_spent) / current_day
    return Decimal(str(rate * days_in_month))


def fintech_worker_budget_velocity_insight(
    user_id: str,
    transactions: list[Transaction],
    budgets: list[BudgetSetting],
    today: date | None = None,
) -> BudgetInsightResponse:
    """Calculate budget velocity insight for a set of transactions and budgets."""
    if today is None:
        today = date.today()

    days_in_month = _days_in_current_month(today)
    current_day = today.day if today.day <= days_in_month else days_in_month
    current_day = max(current_day, 1)

    spending = _category_spending_so_far(transactions, end_date=today)

    # Build a map of category -> budget setting
    budget_map = {b.category: b for b in budgets}

    # Identify the most at-risk category:
    worst_category = ""
    worst_projected_total = Decimal("0")
    worst_deviation = Decimal("0")
    worst_deviation_pct = Decimal("0")

    month_start = date(today.year, today.month, 1)

    for category, total_spent in spending.items():
        budget = budget_map.get(category)
        if budget is None:
            continue

        projected = _projected_spend(total_spent, current_day, days_in_month)
        deviation = projected - budget.monthly_limit
        deviation_pct = deviation / budget.monthly_limit if budget.monthly_limit > 0 else Decimal("0")

        if deviation > worst_deviation:
            worst_deviation = deviation
            worst_deviation_pct = deviation_pct
            worst_projected_total = projected
            worst_category = category

    if not worst_category:
        return BudgetInsightResponse(
            user_id=user_id,
            report_date=today,
            projected_month_end_total=Decimal("0"),
            budget_total=Decimal("0"),
            deviation_pct=Decimal("0"),
            deviation_amount=Decimal("0"),
            threat_level=ThreatLevel.GOOD,
            action_items=ActionItem(),
        )

    # Determine threat level
    if worst_deviation > 0:
        threat_level = ThreatLevel.OVERRUN
    elif worst_deviation_pct > Decimal("0.20"):
        threat_level = ThreatLevel.CRITICAL
    elif worst_deviation_pct > Decimal("0.10"):
        threat_level = ThreatLevel.WARNING
    else:
        threat_level = ThreatLevel.GOOD

    return BudgetInsightResponse(
        user_id=user_id,
        report_date=today,
        projected_month_end_total=worst_projected_total,
        budget_total=budget_map[worst_category].monthly_limit,
        deviation_pct=worst_deviation_pct,
        deviation_amount=worst_deviation,
        threat_level=threat_level,
        action_items=[],  # Empty, to be populated by Bedrock
    )



def _action_items_for_deviation(category: str, deviation: Decimal) -> list[ActionItem]:
    """Generate actionable suggestions based on the category and deviation."""
    actions = []  # type: list[ActionItem]
    if category == "dining":
        actions.append(
            ActionItem(
                action="Skip one restaurant meal this week and cook at home.",
                projected_savings=Decimal("85.00"),
                reason="Projected to exceed dining budget.",
            )
        )
        actions.append(
            ActionItem(
                action="Opt for a mid-range restaurant instead of high-end on weekends.",
                projected_savings=Decimal("120.00"),
                reason="Save 20% per outing by choosing a lower-tier option.",
            )
        )
    elif category == "groceries":
        actions.append(
            ActionItem(
                action="Switch to store-brand products and buy in bulk.",
                projected_savings=Decimal("60.00"),
                reason="Groceries projected to exceed target.",
            )
        )
    else:
        actions.append(
            ActionItem(
                action="Review non-essential spending and delay non-urgent purchases.",
                projected_savings=Decimal("50.00"),
                reason=f"{category.title()} projected to exceed budget.",
            )
        )
    return actions


def _handle_action_items(
    budget: BudgetSetting, projected_month_end_total: Decimal, deviation: Decimal
) -> list[ActionItem]:
    """Generate action items based on budget projected totals."""
    # TODO: refactor into _action_items_for_deviation
    pass

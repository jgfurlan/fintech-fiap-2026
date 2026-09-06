package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.ActionItemDto;
import br.com.fiap.fintech.dto.BudgetInsightResponse;
import br.com.fiap.fintech.dto.ThreatLevel;
import br.com.fiap.fintech.entity.BudgetSettingEntity;
import br.com.fiap.fintech.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Component
public class FintechBudgetVelocityWorker {

    public record WorkerResult(
            String worstCategory,
            BigDecimal worstProjectedTotal,
            BigDecimal budgetTotal,
            BigDecimal worstDeviation,
            BigDecimal worstDeviationPct,
            ThreatLevel threatLevel
    ) {}

    public int getDaysInCurrentMonth(LocalDate date) {
        return YearMonth.from(date).lengthOfMonth();
    }

    public Map<String, BigDecimal> getCategorySpendingSoFar(List<TransactionEntity> transactions, LocalDate endDate) {
        Map<String, BigDecimal> spending = new HashMap<>();
        for (TransactionEntity tx : transactions) {
            if (tx.getDate() != null && !tx.getDate().isAfter(endDate)) {
                spending.merge(tx.getCategory(), tx.getAmount(), BigDecimal::add);
            }
        }
        return spending;
    }

    public BigDecimal calculateProjectedSpend(BigDecimal currentSpent, int currentDay, int daysInMonth) {
        if (currentDay <= 0) {
            return currentSpent;
        }
        // Matching Python: rate = int(current_spent) / current_day; rate * days_in_month
        double rate = currentSpent.doubleValue() / currentDay;
        double projected = rate * daysInMonth;
        return BigDecimal.valueOf(projected).setScale(2, RoundingMode.HALF_UP);
    }

    public WorkerResult calculateInsight(
            List<TransactionEntity> transactions,
            List<BudgetSettingEntity> budgets,
            LocalDate today
    ) {
        int daysInMonth = getDaysInCurrentMonth(today);
        int currentDay = Math.min(today.getDayOfMonth(), daysInMonth);
        currentDay = Math.max(currentDay, 1);

        Map<String, BigDecimal> spending = getCategorySpendingSoFar(transactions, today);

        Map<String, BudgetSettingEntity> budgetMap = new HashMap<>();
        for (BudgetSettingEntity b : budgets) {
            budgetMap.put(b.getCategory(), b);
        }

        String worstCategory = "";
        BigDecimal worstProjectedTotal = BigDecimal.ZERO;
        BigDecimal worstDeviation = BigDecimal.ZERO;
        BigDecimal worstDeviationPct = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> entry : spending.entrySet()) {
            String category = entry.getKey();
            BigDecimal totalSpent = entry.getValue();
            BudgetSettingEntity budget = budgetMap.get(category);
            if (budget == null) {
                continue;
            }

            BigDecimal projected = calculateProjectedSpend(totalSpent, currentDay, daysInMonth);
            BigDecimal deviation = projected.subtract(budget.getMonthlyLimit());
            BigDecimal deviationPct = budget.getMonthlyLimit().compareTo(BigDecimal.ZERO) > 0
                    ? deviation.divide(budget.getMonthlyLimit(), 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            if (deviation.compareTo(worstDeviation) > 0) {
                worstDeviation = deviation;
                worstDeviationPct = deviationPct;
                worstProjectedTotal = projected;
                worstCategory = category;
            }
        }

        if (worstCategory.isEmpty()) {
            return new WorkerResult(
                    "",
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    ThreatLevel.GOOD
            );
        }

        ThreatLevel threatLevel;
        if (worstDeviation.compareTo(BigDecimal.ZERO) > 0) {
            threatLevel = ThreatLevel.OVERRUN;
        } else if (worstDeviationPct.compareTo(BigDecimal.valueOf(0.20)) > 0) {
            threatLevel = ThreatLevel.CRITICAL;
        } else if (worstDeviationPct.compareTo(BigDecimal.valueOf(0.10)) > 0) {
            threatLevel = ThreatLevel.WARNING;
        } else {
            threatLevel = ThreatLevel.GOOD;
        }

        BigDecimal budgetTotal = budgetMap.get(worstCategory).getMonthlyLimit().setScale(2, RoundingMode.HALF_UP);

        return new WorkerResult(
                worstCategory,
                worstProjectedTotal.setScale(2, RoundingMode.HALF_UP),
                budgetTotal,
                worstDeviation.setScale(2, RoundingMode.HALF_UP),
                worstDeviationPct.setScale(2, RoundingMode.HALF_UP),
                threatLevel
        );
    }

    public List<ActionItemDto> getFallbackActionItems(String category, BigDecimal deviation) {
        List<ActionItemDto> actions = new ArrayList<>();
        if ("dining".equalsIgnoreCase(category)) {
            actions.add(new ActionItemDto(
                    "Skip one restaurant meal this week and cook at home.",
                    BigDecimal.valueOf(85.00).setScale(2, RoundingMode.HALF_UP),
                    "Projected to exceed dining budget."
            ));
            actions.add(new ActionItemDto(
                    "Opt for a mid-range restaurant instead of high-end on weekends.",
                    BigDecimal.valueOf(120.00).setScale(2, RoundingMode.HALF_UP),
                    "Save 20% per outing by choosing a lower-tier option."
            ));
        } else if ("groceries".equalsIgnoreCase(category)) {
            actions.add(new ActionItemDto(
                    "Switch to store-brand products and buy in bulk.",
                    BigDecimal.valueOf(60.00).setScale(2, RoundingMode.HALF_UP),
                    "Groceries projected to exceed target."
            ));
        } else {
            actions.add(new ActionItemDto(
                    "Review non-essential spending and delay non-urgent purchases.",
                    BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP),
                    category + " projected to exceed budget."
            ));
        }
        return actions;
    }
}

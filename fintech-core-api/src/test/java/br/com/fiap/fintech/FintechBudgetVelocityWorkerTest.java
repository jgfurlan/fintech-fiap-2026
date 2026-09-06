package br.com.fiap.fintech;

import br.com.fiap.fintech.dto.ThreatLevel;
import br.com.fiap.fintech.entity.BudgetSettingEntity;
import br.com.fiap.fintech.entity.TransactionEntity;
import br.com.fiap.fintech.service.FintechBudgetVelocityWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FintechBudgetVelocityWorkerTest {

    private FintechBudgetVelocityWorker worker;

    @BeforeEach
    void setUp() {
        worker = new FintechBudgetVelocityWorker();
    }

    @Test
    @DisplayName("Worker detects overrun threat level when projected spend exceeds budget")
    void testOverrunInsight() {
        List<TransactionEntity> transactions = List.of(
                new TransactionEntity(1, LocalDate.of(2026, 5, 5), BigDecimal.valueOf(300.00), "dining", "Meal 1"),
                new TransactionEntity(2, LocalDate.of(2026, 5, 10), BigDecimal.valueOf(350.00), "dining", "Meal 2")
        );

        List<BudgetSettingEntity> budgets = List.of(
                new BudgetSettingEntity("dining", BigDecimal.valueOf(600.00), BigDecimal.valueOf(0.80))
        );

        // Day 10 in 31-day month: spent 650 in 10 days -> rate 65/day -> projected ~2015.00 -> overrun
        FintechBudgetVelocityWorker.WorkerResult result = worker.calculateInsight(
                transactions,
                budgets,
                LocalDate.of(2026, 5, 10)
        );

        assertEquals("dining", result.worstCategory());
        assertEquals(ThreatLevel.OVERRUN, result.threatLevel());
        assertTrue(result.worstDeviation().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.worstProjectedTotal().compareTo(BigDecimal.valueOf(600.00)) > 0);
    }

    @Test
    @DisplayName("Worker returns GOOD threat level when spend is well under limit")
    void testGoodInsight() {
        List<TransactionEntity> transactions = List.of(
                new TransactionEntity(1, LocalDate.of(2026, 5, 5), BigDecimal.valueOf(50.00), "transport", "Bus")
        );

        List<BudgetSettingEntity> budgets = List.of(
                new BudgetSettingEntity("transport", BigDecimal.valueOf(500.00), BigDecimal.valueOf(0.80))
        );

        FintechBudgetVelocityWorker.WorkerResult result = worker.calculateInsight(
                transactions,
                budgets,
                LocalDate.of(2026, 5, 15)
        );

        assertEquals(ThreatLevel.GOOD, result.threatLevel());
        assertEquals(BigDecimal.ZERO.setScale(2), result.worstDeviation());
        assertTrue(result.worstProjectedTotal().compareTo(BigDecimal.valueOf(500.00)) <= 0);
    }

    @Test
    @DisplayName("Worker provides non-empty fallback action items for over-budget categories")
    void testFallbackActionItems() {
        var items = worker.getFallbackActionItems("dining", BigDecimal.valueOf(100.00));
        assertFalse(items.isEmpty());
        assertNotNull(items.get(0).action());
        assertNotNull(items.get(0).projectedSavings());
        assertNotNull(items.get(0).reason());
    }
}

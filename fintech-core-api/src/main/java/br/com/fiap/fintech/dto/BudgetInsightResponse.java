package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record BudgetInsightResponse(
        @JsonProperty("user_id") String userId,
        @JsonProperty("report_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate reportDate,
        @JsonProperty("currency") String currency,
        @JsonProperty("projected_month_end_total") BigDecimal projectedMonthEndTotal,
        @JsonProperty("budget_total") BigDecimal budgetTotal,
        @JsonProperty("deviation_pct") BigDecimal deviationPct,
        @JsonProperty("deviation_amount") BigDecimal deviationAmount,
        @JsonProperty("threat_level") String threatLevel,
        @JsonProperty("action_items") List<ActionItemDto> actionItems,
        @JsonProperty("generated_at") @JsonFormat(shape = JsonFormat.Shape.STRING) Instant generatedAt
) {}

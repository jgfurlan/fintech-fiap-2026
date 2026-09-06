package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record ActionItemDto(
        @JsonProperty("action") String action,
        @JsonProperty("projected_savings") BigDecimal projectedSavings,
        @JsonProperty("reason") String reason
) {}

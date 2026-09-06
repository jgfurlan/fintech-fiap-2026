package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ThreatLevel {
    GOOD("good"),
    WARNING("warning"),
    CRITICAL("critical"),
    OVERRUN("overrun");

    private final String value;

    ThreatLevel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

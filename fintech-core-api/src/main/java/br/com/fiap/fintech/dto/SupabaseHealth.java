package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SupabaseHealth(
        @JsonProperty("reachable") boolean reachable,
        @JsonProperty("error") String error,
        @JsonProperty("url_configured") boolean urlConfigured
) {}

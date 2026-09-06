package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HealthResponse(
        @JsonProperty("status") String status,
        @JsonProperty("supabase") SupabaseHealth supabase
) {}

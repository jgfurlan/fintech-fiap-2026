package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
        @JsonProperty("id") String id,
        @JsonProperty("email") String email
) {}

package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RootResponse(
        @JsonProperty("message") String message,
        @JsonProperty("version") String version
) {}

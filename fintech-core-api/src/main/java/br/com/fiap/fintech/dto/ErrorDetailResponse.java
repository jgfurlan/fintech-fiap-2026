package br.com.fiap.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorDetailResponse(
        @JsonProperty("detail") String detail
) {}

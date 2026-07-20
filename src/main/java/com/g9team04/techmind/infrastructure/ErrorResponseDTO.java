package com.g9team04.techmind.infrastructure;

import java.time.Instant;
import java.util.Map;

public record ErrorResponseDTO(
        String code,
        String message,
        Instant timestamp,
        Map<String, String> fieldsErros
) {
    public ErrorResponseDTO(String code, String message) {
        this(code, message, Instant.now(), null);
    }
}

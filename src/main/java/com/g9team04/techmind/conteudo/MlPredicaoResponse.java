package com.g9team04.techmind.conteudo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MlPredicaoResponse(
        String categoria,
        double confianca,
        @JsonProperty("palavras_chave") List<String> palavrasChave
) {
    public MlPredicaoResponse(String texto) {
        this(null, 0.0, List.of());
    }
}

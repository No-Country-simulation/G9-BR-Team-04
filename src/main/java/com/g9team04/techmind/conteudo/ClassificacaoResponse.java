package com.g9team04.techmind.conteudo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ClassificacaoResponse(
        String categoria,
        Double probabilidade,
        @JsonProperty("informacoes_adicionais")
        List<String> tags
) {
}

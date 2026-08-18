package com.g9team04.techmind.conteudo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record ConteudoResponse(
        Long id,
        String titulo,
        String texto,
        String categoria,
        Double probabilidade,
        @JsonProperty("informacoes_adicionais")
        List<String> informacoesAdicionais,
        LocalDateTime criadoEm
) {

}

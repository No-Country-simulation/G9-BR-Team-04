package com.g9team04.techmind.conteudo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ConteudoResponse(
        @JsonIgnore
        Long id,
        @JsonIgnore
        String titulo,
        String categoria,
        Double probabilidade,
        @JsonProperty("informacoes_adicionais")
        List<String> informacoesAdicionais
) {

}

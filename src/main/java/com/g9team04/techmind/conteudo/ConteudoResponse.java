package com.g9team04.techmind.conteudo;

import java.util.List;

public record ConteudoResponse(
        String categoria,
        Double probabilidade,
        List<String> informacoesAdicionais
) {
}

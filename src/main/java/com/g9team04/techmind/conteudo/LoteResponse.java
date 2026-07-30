package com.g9team04.techmind.conteudo;

import java.util.List;

public record LoteResponse(
        int totalLinhas,
        int sucessos,
        int falhas,
        List<Long> idsProcessados,
        List<ErroLinha> erros
){
    public record ErroLinha(
            int linha,
            String titulo,
            String mensagem
    ){}

}

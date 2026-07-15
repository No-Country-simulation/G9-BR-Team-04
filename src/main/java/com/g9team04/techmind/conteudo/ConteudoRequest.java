package com.g9team04.techmind.conteudo;

import jakarta.validation.constraints.NotBlank;

public record ConteudoRequest(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "O texto é obrigatório")
        String texto
) {
}

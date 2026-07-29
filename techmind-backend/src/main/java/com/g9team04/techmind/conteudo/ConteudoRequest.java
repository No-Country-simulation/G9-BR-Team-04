package com.g9team04.techmind.conteudo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ConteudoRequest(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve possuir no máximo 150 caracteres.")
        String titulo,

        @NotBlank(message = "O texto é obrigatório")
        String texto,
         @Size(max = 100, message = "A fonte deve possuir no máximo 100 caracteres.")
        String fonte,
        @URL(message = "A URL informada é inválida.")
        String url
) {
}

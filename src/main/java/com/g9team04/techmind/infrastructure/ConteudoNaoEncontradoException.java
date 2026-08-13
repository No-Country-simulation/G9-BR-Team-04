package com.g9team04.techmind.infrastructure;

import org.springframework.http.HttpStatus;

public class ConteudoNaoEncontradoException extends ApplicationException {

    public ConteudoNaoEncontradoException(Long id) {
        super("Conteúdo com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);

    }

    public ConteudoNaoEncontradoException(String categoria) {
        super("Categoria '" + categoria + "' não encontrada.", HttpStatus.NOT_FOUND);
    }
}

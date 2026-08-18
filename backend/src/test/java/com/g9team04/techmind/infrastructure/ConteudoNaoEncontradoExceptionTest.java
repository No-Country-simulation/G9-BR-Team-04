package com.g9team04.techmind.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ConteudoNaoEncontradoExceptionTest {

    @Test
    void deveConterMensagemComIdEStatusNotFound() {
        var exception = new ConteudoNaoEncontradoException(42L);

        assertThat(exception.getMessage()).isEqualTo("Conteúdo com ID 42 não encontrado.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deveConterMensagemComCategoriaEStatusNotFound() {
        var exception = new ConteudoNaoEncontradoException("Backend");

        assertThat(exception.getMessage()).isEqualTo("Categoria 'Backend' não encontrada.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deveSerUmaApplicationException() {
        var exception = new ConteudoNaoEncontradoException(1L);

        assertThat(exception).isInstanceOf(ApplicationException.class);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
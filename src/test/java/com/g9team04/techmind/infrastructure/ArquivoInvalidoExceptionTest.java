package com.g9team04.techmind.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ArquivoInvalidoExceptionTest {

    @Test
    void deveConterMensagemEStatusBadRequest() {
        var exception = new ArquivoInvalidoException("Arquivo vazio ou não enviado.");

        assertThat(exception.getMessage()).isEqualTo("Arquivo vazio ou não enviado.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deveConterMensagemStatusECausaQuandoConstruidaComCause() {
        var causaOriginal = new RuntimeException("erro interno");
        var exception = new ArquivoInvalidoException("Erro ao processar arquivo.", causaOriginal);

        assertThat(exception.getMessage()).isEqualTo("Erro ao processar arquivo.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getCause()).isEqualTo(causaOriginal);
    }

    @Test
    void deveSerUmaRuntimeException() {
        var exception = new ArquivoInvalidoException("qualquer mensagem");

        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception).isInstanceOf(ApplicationException.class);
    }
}

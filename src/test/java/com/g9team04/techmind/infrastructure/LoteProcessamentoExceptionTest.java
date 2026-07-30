package com.g9team04.techmind.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class LoteProcessamentoExceptionTest {

    @Test
    void deveConterMensagemEStatusBadRequestSemCause() {
        var exception = new LoteProcessamentoException("Erro ao processar lote.");

        assertThat(exception.getMessage()).isEqualTo("Erro ao processar lote.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void deveConterMensagemStatusECausaQuandoConstruidaComCause() {
        var causaOriginal = new RuntimeException("falha no parser");
        var exception = new LoteProcessamentoException("Erro ao processar lote.", causaOriginal);

        assertThat(exception.getMessage()).isEqualTo("Erro ao processar lote.");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getCause()).isEqualTo(causaOriginal);
    }

    @Test
    void deveSerUmaApplicationException() {
        var exception = new LoteProcessamentoException("qualquer mensagem");

        assertThat(exception).isInstanceOf(ApplicationException.class);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
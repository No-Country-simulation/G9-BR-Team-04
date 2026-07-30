package com.g9team04.techmind.conteudo.internal;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HashUtilsTest {

    @Test
    void deveGerarMesmoHashParaMesmoTexto() {
        var hash1 = HashUtils.sha256("abc");
        var hash2 = HashUtils.sha256("abc");

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void deveGerarHashDiferenteParaTextosDiferentes() {
        var hash1 = HashUtils.sha256("abc");
        var hash2 = HashUtils.sha256("abd");

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void hashDeveTer64Caracteres() {
        // SHA-256 sempre gera 32 bytes = 64 caracteres em hexadecimal
        var hash = HashUtils.sha256("qualquer texto");

        assertThat(hash).hasSize(64);
    }

    @Test
    void deveGerarHashParaTextoVazio() {
        // testa o caso "extremo" — string vazia não deveria quebrar o método
        var hash = HashUtils.sha256("");

        assertThat(hash).isNotNull();
        assertThat(hash).hasSize(64);
    }
}

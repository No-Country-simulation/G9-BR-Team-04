package com.g9team04.techmind.conteudo.internal;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringListConverterTest {

    private final StringListConverter converter = new StringListConverter();

    @Test
    void deveConverterListaParaJsonEVoltarParaLista() {
        var lista = List.of("Java", "Spring Boot", "API REST");

        var json = converter.convertToDatabaseColumn(lista);
        var resultado = converter.convertToEntityAttribute(json);

        assertThat(resultado).containsExactlyElementsOf(lista);
    }

    @Test
    void deveRetornarColchetesVaziosQuandoListaForNula() {
        var json = converter.convertToDatabaseColumn(null);

        assertThat(json).isEqualTo("[]");
    }

    @Test
    void deveRetornarColchetesVaziosQuandoListaForVazia() {
        var json = converter.convertToDatabaseColumn(List.of());

        assertThat(json).isEqualTo("[]");
    }

    @Test
    void deveRetornarListaVaziaQuandoStringForNula() {
        var resultado = converter.convertToEntityAttribute(null);

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveRetornarListaVaziaQuandoStringForEmBranco() {
        var resultado = converter.convertToEntityAttribute("   ");

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveLancarExcecaoQuandoJsonForInvalido() {
        assertThatThrownBy(() -> converter.convertToEntityAttribute("{isso não é uma lista válida"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao desserializar tags");
    }
}

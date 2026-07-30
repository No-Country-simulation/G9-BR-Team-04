package com.g9team04.techmind.conteudo.internal;

import com.g9team04.techmind.conteudo.ClassifierService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OciClassifierServiceTest {

    private final OciClassifierService classifierService = new OciClassifierService();

    @Test
    void deveRetornarClassificacaoNaoNulaParaQualquerTexto() {
        var resultado = classifierService.classificar("Texto qualquer sobre Java e Spring Boot");

        assertThat(resultado).isNotNull();
    }

    @Test
    void deveRetornarCategoriaEProbabilidadePreenchidas() {
        var resultado = classifierService.classificar("Texto de exemplo");

        assertThat(resultado.categoria()).isNotBlank();
        assertThat(resultado.probabilidade()).isBetween(0.0, 1.0);
    }

    @Test
    void deveRetornarListaDeTagsNaoVazia() {
        var resultado = classifierService.classificar("Texto de exemplo");

        assertThat(resultado.tags()).isNotEmpty();
    }

    @Test
    void deveImplementarAInterfaceClassifierService() {
        assertThat(classifierService).isInstanceOf(ClassifierService.class);
    }
}

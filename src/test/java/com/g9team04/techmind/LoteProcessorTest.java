package com.g9team04.techmind;

import com.g9team04.techmind.conteudo.ConteudoRequest;
import com.g9team04.techmind.conteudo.ConteudoResponse;
import com.g9team04.techmind.conteudo.ConteudoService;
import com.g9team04.techmind.conteudo.LoteProcessor;
import com.g9team04.techmind.conteudo.LoteResponse;
import com.g9team04.techmind.infrastructure.LoteProcessamentoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Testes do LoteProcessor cobrindo os cenários já validados manualmente:
 * sucesso, coluna faltando, campo vazio, linha em branco e lote totalmente falho.
 */
@ExtendWith(MockitoExtension.class)
class LoteProcessorTest {

    @Mock
    private ConteudoService conteudoService;

    @InjectMocks
    private LoteProcessor loteProcessor;

    private InputStream csv(String conteudo) {
        return new ByteArrayInputStream(conteudo.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void deveProcessarTodasAsLinhasComSucesso() {
        String conteudoCsv = """
                titulo,texto
                Introdução ao Spring Boot,Spring Boot facilita a criação de APIs.
                Java Streams,Streams permitem programação funcional.
                """;

        when(conteudoService.processar(any(ConteudoRequest.class)))
                .thenReturn(new ConteudoResponse(1L, "titulo1", "Backend", 0.9, java.util.List.of("java")))
                .thenReturn(new ConteudoResponse(2L, "titulo2", "Backend", 0.9, java.util.List.of("java")));

        LoteResponse response = loteProcessor.processar(csv(conteudoCsv));

        assertThat(response.totalLinhas()).isEqualTo(2);
        assertThat(response.sucessos()).isEqualTo(2);
        assertThat(response.falhas()).isZero();
        assertThat(response.idsProcessados()).containsExactly(1L, 2L);
        assertThat(response.erros()).isEmpty();
    }

    @Test
    void deveReportarErroQuandoColunaEstaFaltando() {
        // Inclui 1 linha válida para não disparar a regra de "lote totalmente falho",
        // que é testada separadamente em deveLancarExcecaoQuandoLoteEstaTotalmenteFalho().
        String conteudoCsv = """
                titulo,texto
                Introdução ao Spring Boot
                Java Streams
                Kubernetes na prática,Orquestração de containers em produção.
                """;

        when(conteudoService.processar(any(ConteudoRequest.class)))
                .thenReturn(new ConteudoResponse(1L, "t1", "DevOps", 0.9, java.util.List.of()));

        LoteResponse response = loteProcessor.processar(csv(conteudoCsv));

        assertThat(response.totalLinhas()).isEqualTo(3);
        assertThat(response.sucessos()).isEqualTo(1);
        assertThat(response.falhas()).isEqualTo(2);
        assertThat(response.erros())
                .extracting(LoteResponse.ErroLinha::mensagem)
                .containsOnly("Linha com menos de 2 colunas");
    }

    @Test
    void deveReportarErroQuandoTituloOuTextoEstaoVazios() {
        // Inclui 1 linha válida para não disparar a regra de "lote totalmente falho",
        // que é testada separadamente em deveLancarExcecaoQuandoLoteEstaTotalmenteFalho().
        String conteudoCsv = """
                titulo,texto
                ,Texto sem título
                Título sem texto,
                Kubernetes na prática,Orquestração de containers em produção.
                """;

        when(conteudoService.processar(any(ConteudoRequest.class)))
                .thenReturn(new ConteudoResponse(1L, "t1", "DevOps", 0.9, java.util.List.of()));

        LoteResponse response = loteProcessor.processar(csv(conteudoCsv));

        assertThat(response.totalLinhas()).isEqualTo(3);
        assertThat(response.sucessos()).isEqualTo(1);
        assertThat(response.falhas()).isEqualTo(2);
        assertThat(response.erros())
                .extracting(LoteResponse.ErroLinha::mensagem)
                .containsOnly("Título ou texto vazio");
    }

    @Test
    void deveReportarErroQuandoHaLinhaEmBranco() {
        String conteudoCsv = "titulo,texto\n"
                + "Introdução ao Spring Boot,Spring Boot facilita a criação de APIs.\n"
                + "\n"
                + "Java Streams,Streams permitem programação funcional.\n";

        when(conteudoService.processar(any(ConteudoRequest.class)))
                .thenReturn(new ConteudoResponse(1L, "t1", "Backend", 0.9, java.util.List.of()))
                .thenReturn(new ConteudoResponse(2L, "t2", "Backend", 0.9, java.util.List.of()));

        LoteResponse response = loteProcessor.processar(csv(conteudoCsv));

        assertThat(response.totalLinhas()).isEqualTo(3);
        assertThat(response.sucessos()).isEqualTo(2);
        assertThat(response.falhas()).isEqualTo(1);
        assertThat(response.erros().get(0).linha()).isEqualTo(2);
        assertThat(response.erros().get(0).mensagem()).isEqualTo("Linha com menos de 2 colunas");
    }

    @Test
    void deveLancarExcecaoQuandoLoteEstaTotalmenteFalho() {
        String conteudoCsv = """
                titulo
                Introdução ao Spring Boot
                Java Streams
                """;

        assertThatThrownBy(() -> loteProcessor.processar(csv(conteudoCsv)))
                .isInstanceOf(LoteProcessamentoException.class)
                .hasMessageContaining("Nenhuma linha do arquivo pôde ser processada");
    }

    @Test
    void deveRetornarZeradoQuandoArquivoNaoTemLinhasDeDados() {
        String conteudoCsv = "titulo,texto\n";

        LoteResponse response = loteProcessor.processar(csv(conteudoCsv));

        assertThat(response.totalLinhas()).isZero();
        assertThat(response.sucessos()).isZero();
        assertThat(response.falhas()).isZero();
        assertThat(response.idsProcessados()).isEmpty();
        assertThat(response.erros()).isEmpty();
    }

    @Test
    void deveIsolarErroDeUmaLinhaSemDerrubarAsDemais() {
        String conteudoCsv = """
                titulo,texto
                Introdução ao Spring Boot,Spring Boot facilita a criação de APIs.
                Java Streams,Streams permitem programação funcional.
                """;

        when(conteudoService.processar(any(ConteudoRequest.class)))
                .thenReturn(new ConteudoResponse(1L, "t1", "Backend", 0.9, java.util.List.of()))
                .thenThrow(new RuntimeException("Falha simulada no classificador"));

        LoteResponse response = loteProcessor.processar(csv(conteudoCsv));

        assertThat(response.totalLinhas()).isEqualTo(2);
        assertThat(response.sucessos()).isEqualTo(1);
        assertThat(response.falhas()).isEqualTo(1);
        assertThat(response.erros().get(0).mensagem()).contains("Falha simulada no classificador");
    }
}
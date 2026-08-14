package com.g9team04.techmind.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class ValidatorCsvTest {

    private final ValidatorCsv validator = new ValidatorCsv();

    @Test
    void deveValidarCsvComFormatoCorreto() {
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.csv", "text/csv", "titulo,texto\nA,B".getBytes()
        );

        assertThatCode(() -> validator.validarCsv(arquivo)).doesNotThrowAnyException();
    }

    @Test
    void deveAceitarCsvComContentTypeMsExcel() {
        // caso especial permitido pelo código: navegadores/Excel às vezes enviam esse content-type pra CSV
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.csv", "application/vnd.ms-excel", "titulo,texto\nA,B".getBytes()
        );

        assertThatCode(() -> validator.validarCsv(arquivo)).doesNotThrowAnyException();
    }

    @Test
    void deveAceitarCsvComContentTypeOctetStream() {
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.csv", "application/octet-stream", "titulo,texto\nA,B".getBytes()
        );

        assertThatCode(() -> validator.validarCsv(arquivo)).doesNotThrowAnyException();
    }

    @Test
    void deveLancarExcecaoQuandoArquivoForNulo() {
        assertThatThrownBy(() -> validator.validarCsv(null))
                .isInstanceOf(ArquivoInvalidoException.class)
                .hasMessageContaining("Arquivo vazio ou não enviado");
    }

    @Test
    void deveLancarExcecaoQuandoArquivoForVazio() {
        var arquivo = new MockMultipartFile("arquivo", "dados.csv", "text/csv", new byte[0]);

        assertThatThrownBy(() -> validator.validarCsv(arquivo))
                .isInstanceOf(ArquivoInvalidoException.class)
                .hasMessageContaining("Arquivo vazio ou não enviado");
    }

    @Test
    void deveLancarExcecaoQuandoExtensaoForInvalida() {
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.txt", "text/csv", "titulo,texto\nA,B".getBytes()
        );

        assertThatThrownBy(() -> validator.validarCsv(arquivo))
                .isInstanceOf(ArquivoInvalidoException.class)
                .hasMessageContaining("Extensão de arquivo inválida");
    }

    @Test
    void deveLancarExcecaoQuandoNomeDoArquivoForNulo() {
        var arquivo = new MockMultipartFile(
                "arquivo", null, "text/csv", "titulo,texto\nA,B".getBytes()
        );

        assertThatThrownBy(() -> validator.validarCsv(arquivo))
                .isInstanceOf(ArquivoInvalidoException.class)
                .hasMessageContaining("Extensão de arquivo inválida");
    }

    @Test
    void deveLancarExcecaoQuandoContentTypeForIncompativel() {
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.csv", "image/png", "titulo,texto\nA,B".getBytes()
        );

        assertThatThrownBy(() -> validator.validarCsv(arquivo))
                .isInstanceOf(ArquivoInvalidoException.class)
                .hasMessageContaining("Tipo de arquivo não reconhecido");
    }

    @Test
    void deveLancarExcecaoQuandoArquivoExcederTamanhoMaximo() {
        byte[] conteudoGrande = new byte[11 * 1024 * 1024]; // 11 MB > limite de 10 MB
        var arquivo = new MockMultipartFile("arquivo", "dados.csv", "text/csv", conteudoGrande);

        assertThatThrownBy(() -> validator.validarCsv(arquivo))
                .isInstanceOf(ArquivoInvalidoException.class)
                .hasMessageContaining("Arquivo muito grande");
    }
}

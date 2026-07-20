package com.g9team04.techmind.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ValidatorCsv {
    // Constantes de tamanho
    private static final Long MAX_REQUEST_SIZE = 10L * 1024 * 1024L; // 10 MB
    // TODO: reservado para validação de timeout de processamento (não implementado ainda)
    // private static final Long MAX_REQUEST_TIME_OUT_MS = ...;

    // Constantes de tipos e extensões
    private static final String CONTENT_TYPE_CSV = "text/csv";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_MS_EXCEL = "application/vnd.ms-excel";
    private static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
    private static final String CSV_EXTENSION = ".csv";
    private static final String JSON_EXTENSION = ".json";

    /**
     * Valida arquivo CSV.
     * @param arquivo MultipartFile recebido na requisição
     * @throws ArquivoInvalidoException se o arquivo não for um CSV válido
     */
    public void validarCsv(MultipartFile arquivo) {
        validarArquivo(arquivo, CONTENT_TYPE_CSV, CSV_EXTENSION);
    }

    /**
     * Valida arquivo JSON (preparado para uso futuro).
     * @param arquivo MultipartFile recebido na requisição
     * @throws ArquivoInvalidoException se o arquivo não for um JSON válido
     */
    public void validarJson(MultipartFile arquivo) {
        validarArquivo(arquivo, CONTENT_TYPE_JSON, JSON_EXTENSION);
    }

    /**
     * Método genérico de validação.
     */
    private void validarArquivo(MultipartFile arquivo, String contentTypeEsperado, String extensaoEsperada) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoInvalidoException("Arquivo vazio ou não enviado.");
        }

        String nome = arquivo.getOriginalFilename();
        String contentType = arquivo.getContentType();

        // Valida extensão
        if (nome == null || !nome.toLowerCase().endsWith(extensaoEsperada)) {
            throw new ArquivoInvalidoException(
                    String.format("Extensão de arquivo inválida. Esperado: %s.", extensaoEsperada)
            );
        }

        // Valida Content-Type (se presente)
        if (contentType != null && !contentType.equals(contentTypeEsperado)) {
            boolean tipoAceitavel = extensaoEsperada.equals(CSV_EXTENSION)
                    && (contentType.equals(CONTENT_TYPE_MS_EXCEL) || contentType.equals(CONTENT_TYPE_OCTET_STREAM));

            if (!tipoAceitavel) {
                throw new ArquivoInvalidoException(
                        String.format("Tipo de arquivo não reconhecido. Esperado: %s.", contentTypeEsperado)
                );
            }
        }

        // Valida tamanho
        if (arquivo.getSize() > MAX_REQUEST_SIZE) {
            throw new ArquivoInvalidoException(
                    String.format("Arquivo muito grande. Tamanho máximo: %d MB.", MAX_REQUEST_SIZE / (1024 * 1024))
            );
        }
    }
}
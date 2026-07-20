package com.g9team04.techmind.conteudo;

import com.g9team04.techmind.infrastructure.LoteProcessamentoException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class LoteProcessor {

    private static final int COLUNAS_MINIMAS = 2;

    private final ConteudoService conteudoService;

    public LoteProcessor(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    public LoteResponse processar(InputStream inputStream) {
        return lerLinhas(inputStream)
                .map(this::processarTodasLinhas)
                .map(this::validarLoteNaoTotalmenteFalho)
                .orElseGet(() -> new LoteResponse(0, 0, 0, List.of(), List.of()));
    }

    /**
     * Lê o CSV inteiro e retorna as linhas de dados (cabeçalho já descartado).
     * Encapsula IOException/CsvException numa exceção de domínio.
     */
    private Optional<List<String[]>> lerLinhas(InputStream inputStream) {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            return Optional.of(csvReader.readAll());

        } catch (IOException | CsvException e) {
            throw new LoteProcessamentoException("Erro ao processar arquivo CSV", e);
        }
    }

    /**
     * Processa todas as linhas e agrega o resultado num LoteResponse,
     * particionando sucessos e falhas de forma funcional.
     */
    private LoteResponse processarTodasLinhas(List<String[]> todasLinhas) {
        int totalLinhas = todasLinhas.size();

        Map<Boolean, List<ResultadoProcessamento>> particionado = IntStream.range(0, totalLinhas)
                .mapToObj(i -> processarLinha(todasLinhas.get(i), i + 1))
                .collect(Collectors.partitioningBy(ResultadoProcessamento::sucesso));

        List<Long> idsProcessados = particionado.get(true).stream()
                .map(ResultadoProcessamento::id)
                .toList();

        List<LoteResponse.ErroLinha> erros = particionado.get(false).stream()
                .map(ResultadoProcessamento::erro)
                .toList();

        return new LoteResponse(totalLinhas, idsProcessados.size(), erros.size(), idsProcessados, erros);
    }

    /**
     * Regra de negócio: se havia linhas de dados e nenhuma processou com sucesso,
     * o lote inteiro é considerado uma falha.
     */
    private LoteResponse validarLoteNaoTotalmenteFalho(LoteResponse response) {
        return Optional.of(response)
                .filter(r -> r.totalLinhas() == 0 || r.sucessos() > 0)
                .orElseThrow(() -> new LoteProcessamentoException(
                        "Nenhuma linha do arquivo pôde ser processada. Verifique o conteúdo e tente novamente."));
    }

    /**
     * Processa uma linha, encadeando validações funcionalmente:
     * primeiro valida colunas, depois campos vazios, só então tenta persistir.
     */
    private ResultadoProcessamento processarLinha(String[] linha, int numeroLinha) {
        return validarQuantidadeColunas(linha, numeroLinha)
                .or(() -> validarCamposObrigatorios(linha, numeroLinha))
                .orElseGet(() -> executarProcessamento(linha, numeroLinha));
    }

    private Optional<ResultadoProcessamento> validarQuantidadeColunas(String[] linha, int numeroLinha) {
        return Optional.of(linha)
                .filter(l -> l.length < COLUNAS_MINIMAS)
                .map(l -> ResultadoProcessamento.erro(numeroLinha, "", "Linha com menos de 2 colunas"));
    }

    private Optional<ResultadoProcessamento> validarCamposObrigatorios(String[] linha, int numeroLinha) {
        String titulo = linha[0].trim();
        String texto = linha[1].trim();

        return Optional.of(titulo)
                .filter(t -> t.isEmpty() || texto.isEmpty())
                .map(t -> ResultadoProcessamento.erro(numeroLinha,
                        t.isEmpty() ? "vazio" : t,
                        "Título ou texto vazio"));
    }

    private ResultadoProcessamento executarProcessamento(String[] linha, int numeroLinha) {
        String titulo = linha[0].trim();
        String texto = linha[1].trim();

        try {
            ConteudoRequest request = new ConteudoRequest(titulo, texto, null, null);
            ConteudoResponse response = conteudoService.processar(request);
            return ResultadoProcessamento.sucesso(response.id());
        } catch (Exception e) {
            return ResultadoProcessamento.erro(numeroLinha, titulo, "Erro ao processar: " + e.getMessage());
        }
    }

    /**
     * Encapsula o resultado do processamento de uma linha.
     */
    private record ResultadoProcessamento(boolean sucesso, Long id, LoteResponse.ErroLinha erro) {

        static ResultadoProcessamento sucesso(Long id) {
            return new ResultadoProcessamento(true, id, null);
        }

        static ResultadoProcessamento erro(int linha, String titulo, String mensagem) {
            return new ResultadoProcessamento(false, null, new LoteResponse.ErroLinha(linha, titulo, mensagem));
        }
    }

}
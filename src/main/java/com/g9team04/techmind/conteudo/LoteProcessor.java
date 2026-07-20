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
import java.util.stream.IntStream;

@Component
public class LoteProcessor {

    private final ConteudoService conteudoService;

    public LoteProcessor(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    public LoteResponse processar(InputStream inputStream) {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            List<String[]> todasLinhas = csvReader.readAll();
            int totalLinhas = todasLinhas.size();

            List<ResultadoProcessamento> resultados = IntStream.range(0, totalLinhas)
                    .mapToObj(i -> processarLinha(todasLinhas.get(i), i + 1))
                    .toList();

            List<Long> idsProcessados = resultados.stream()
                    .filter(ResultadoProcessamento::sucesso)
                    .map(ResultadoProcessamento::id)
                    .toList();

            List<LoteResponse.ErroLinha> erros = resultados.stream()
                    .filter(r -> !r.sucesso())
                    .map(ResultadoProcessamento::erro)
                    .toList();

            int sucessos = idsProcessados.size();
            int falhas = erros.size();
            return new LoteResponse(totalLinhas, sucessos, falhas, idsProcessados, erros);

        } catch (IOException | CsvException e) {
            throw new LoteProcessamentoException("Erro ao processar arquivo CSV", e);
        }
    }

    /**
     * Processa uma linha e retorna um ResultadoProcessamento que indica sucesso ou erro.
     */
    private ResultadoProcessamento processarLinha(String[] linha, int numeroLinha) {
        if (linha.length < 2) {
            return ResultadoProcessamento.erro(numeroLinha, "", "Linha com menos de 2 colunas");
        }

        String titulo = linha[0].trim();
        String texto = linha[1].trim();

        if (titulo.isEmpty() || texto.isEmpty()) {
            return ResultadoProcessamento.erro(numeroLinha,
                    titulo.isEmpty() ? "vazio" : titulo,
                    "Título ou texto vazio");
        }

        try {
            ConteudoRequest request = new ConteudoRequest(titulo, texto, null, null);
            ConteudoResponse response = conteudoService.processar(request);
            return ResultadoProcessamento.sucesso(response.id());
        } catch (Exception e) {
            return ResultadoProcessamento.erro(numeroLinha, titulo,
                    "Erro ao processar: " + e.getMessage());
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
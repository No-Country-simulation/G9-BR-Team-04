package com.g9team04.techmind.conteudo.internal;

import com.g9team04.techmind.conteudo.ClassifierService;
import com.g9team04.techmind.conteudo.MlPredicaoResponse;
import com.g9team04.techmind.infrastructure.MlClassificacaoException;
// 1. Imports do Resilience4j necessários:
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OciClassifierService implements ClassifierService {
    private static final Logger log = LoggerFactory.getLogger(OciClassifierService.class);

    // Constante para mapear o nome das instâncias lá no application.properties
    private static final String INSTANCIA = "classificador";

    private final RestClient restClient;

    public OciClassifierService(RestClient ociClassifierRestClient) {
        this.restClient = ociClassifierRestClient;
    }

    @Override
    // 2. Anotações de resiliência aplicadas na ordem correta
    @Retry(name = INSTANCIA)
    @CircuitBreaker(name = INSTANCIA, fallbackMethod = "fallbackClassificar")
    @Bulkhead(name = INSTANCIA)
    public ClassificacaoResponse classificar(String titulo, String texto) {
        try {
            var resposta = restClient.post()
                    .uri("/predizer")
                    .body(new MlPredicaoRequest(titulo, texto))
                    .retrieve()
                    .body(MlPredicaoResponse.class);

            if (resposta == null) {
                log.error("API de classificação retornou corpo vazio");
                throw new MlClassificacaoException("Resposta vazia da API de classificação.");
            }

            log.info("Classificação recebida: categoria={}, confianca={}",
                    resposta.categoria(), resposta.confianca());

            return new ClassificacaoResponse(
                    resposta.categoria(),
                    resposta.confianca(),
                    resposta.palavrasChave()
            );

        } catch (HttpServerErrorException.ServiceUnavailable e) {
            log.warn("Modelo de classificação ainda não está carregado na API Python");
            throw new MlClassificacaoException("Modelo de classificação ainda não está carregado.", e);

        } catch (HttpStatusCodeException e) {
            log.error("API de classificação retornou erro HTTP {}: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new MlClassificacaoException(
                    "API de classificação retornou erro: " + e.getStatusCode(), e);

        } catch (ResourceAccessException e) {
            log.error("Falha de comunicação com a API de classificação (timeout ou indisponível): {}",
                    e.getMessage());
            throw new MlClassificacaoException("Não foi possível conectar à API de classificação.", e);
        }
    }

    /**
     * 3. Método de Fallback obrigatório para o Circuit Breaker.
     * ATENÇÃO: Os parâmetros devem ser exatamente os mesmos do método original
     * mais o 'Throwable t' no final.
     */
    private ClassificacaoResponse fallbackClassificar(String titulo, String texto, Throwable t) {
        log.error("Fallback acionado para o classificador. Causa: {}", t.getMessage());
        throw new MlClassificacaoException(
                "Serviço de classificação está temporariamente indisponível. Tente novamente em instantes.", t);
    }
}
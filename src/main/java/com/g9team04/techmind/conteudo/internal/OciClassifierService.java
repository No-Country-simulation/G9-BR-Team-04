package com.g9team04.techmind.conteudo.internal;

import com.g9team04.techmind.conteudo.ClassifierService;
import com.g9team04.techmind.conteudo.MlPredicaoResponse;
import com.g9team04.techmind.infrastructure.MlClassificacaoException;
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

    private final RestClient restClient;

    public OciClassifierService(RestClient ociClassifierRestClient) {
        this.restClient = ociClassifierRestClient;
    }

    @Override
    public ClassificacaoResponse classificar(String texto) {
        try {
            var resposta = restClient.post()
                    .uri("/predizer")
                    .body(new MlPredicaoRequest(texto))
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
}

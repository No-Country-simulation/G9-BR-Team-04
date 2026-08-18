package com.g9team04.techmind.conteudo.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class ClassifierRestClientConfig {

    @Bean
    RestClient ociClassifierRestClient(
            @Value("${techmind.classificador.base-url:http://localhost:8000}") String baseUrl,
            @Value("${techmind.classificador.api-key:dev-key-troque-em-producao}") String apiKey) {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1) // evita upgrade h2c, incompatível com uvicorn
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Internal-Api-Key", apiKey)
                .requestFactory(requestFactory)
                .build();
    }
}
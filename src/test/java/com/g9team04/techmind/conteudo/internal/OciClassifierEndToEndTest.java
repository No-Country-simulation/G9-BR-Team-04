package com.g9team04.techmind.conteudo.internal;

import com.g9team04.techmind.conteudo.MlPredicaoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OciClassifierEndToEndTest {

    @LocalServerPort
    private int port;

    private RestClient localRestClient;

    @MockitoBean
    private RestClient restClient; // Mock da API externa Python/Oci

    @BeforeEach
    void setUp() {
        localRestClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @DisplayName("Deve chamar o endpoint REST com sucesso e retornar a classificacao")
    void deveTestarEndpointClassificacaoComSucesso() {
        // 1. Mock do comportamento da IA externa
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(any(String.class))).thenReturn(bodySpec);
        when(bodySpec.body(any(Object.class))).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(any(Class.class)))
                .thenReturn(new MlPredicaoResponse("Java", 0.98, List.of("Spring", "Backend")));

        // 2. Dispara a requisição real para o endpoint CORRETO da aplicação
        //    IMPORTANTE: use um texto único para não bater no cache de testes anteriores
        String payloadJson = """
                {"titulo": "Teste E2E Classificacao", "texto": "Estudando Spring Boot e Java - teste unico %s"}
                """.formatted(System.nanoTime());

        ResponseEntity<String> response = localRestClient.post()
                .uri("/conteudo")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payloadJson)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, responseObj) -> {
                    // Impede que o client lance exceção automática, permitindo ler o corpo do erro
                })
                .toEntity(String.class);

        System.out.println(">>> Status Code do Servidor: " + response.getStatusCode());
        System.out.println(">>> Corpo/Stacktrace do Erro: " + response.getBody());

        // 3. Validações — o endpoint /conteudo retorna 201 Created, não 200
        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCode().value());
    }
}
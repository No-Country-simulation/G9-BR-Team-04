package com.g9team04.techmind.conteudo.internal;

import com.g9team04.techmind.conteudo.ClassifierService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class OciClassifierResilienceTest {

    @Autowired
    private ClassifierService classifierService;

    @MockitoBean
    private RestClient restClient;

    @Test
    @DisplayName("Deve acionar restricoes de resiliencia sob alta concorrencia")
    void deveTestarConcorrenciaEResiliencia() throws InterruptedException {
        // Criamos os mocks tipados das etapas do RestClient
        RestClient.RequestBodyUriSpec uriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec bodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(any(String.class))).thenReturn(bodySpec);
        // Tipamos explicitamente o Object para evitar ambiguidade no método body()
        when(bodySpec.body(any(Object.class))).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(any(Class.class))).thenAnswer(invocation -> {
            Thread.sleep(1000);
            return new com.g9team04.techmind.conteudo.MlPredicaoResponse("Teste", 0.95, java.util.List.of());
        });

        int totalThreads = 35;
        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch latch = new CountDownLatch(totalThreads);

        AtomicInteger sucessos = new AtomicInteger();
        AtomicInteger falhasOuFallback = new AtomicInteger();

        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                try {
                    classifierService.classificar("Texto de teste concorrente");
                    sucessos.incrementAndGet();
                } catch (Exception e) {
                    falhasOuFallback.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println(">>> Sucessos: " + sucessos.get());
        System.out.println(">>> Bloqueados/Fallback: " + falhasOuFallback.get());

        assertTrue(falhasOuFallback.get() > 0, "O Resilience4j deveria ter bloqueado chamadas excedentes!");
    }
}
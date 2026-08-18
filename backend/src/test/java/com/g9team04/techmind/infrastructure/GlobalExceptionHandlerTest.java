package com.g9team04.techmind.infrastructure;

import com.g9team04.techmind.conteudo.ControllerConteudo;
import com.g9team04.techmind.conteudo.ConteudoService;
import com.g9team04.techmind.conteudo.LoteProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerConteudo.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConteudoService conteudoService;

    @MockitoBean
    private LoteProcessor loteProcessor;

    @MockitoBean
    private ValidatorCsv validatorCsv;

    @Test
    void deveRetornarStatusCorretoQuandoApplicationExceptionForLancada() throws Exception {
        when(conteudoService.buscarPorCategoria(eq("Inexistente"), any()))
                .thenThrow(new ConteudoNaoEncontradoException("Inexistente"));

        mockMvc.perform(get("/conteudo/categoria").param("categoria", "Inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Categoria 'Inexistente' não encontrada."));
    }

    @Test
    void deveRetornarBadRequestQuandoValidacaoFalhar() throws Exception {
        // título vazio dispara @NotBlank no ConteudoRequest → MethodArgumentNotValidException
        var requestInvalido = """
                {
                    "titulo": "",
                    "texto": "Algum texto",
                    "fonte": null,
                    "url": null
                }
                """;

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldsErros.titulo").exists());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoInesperada() throws Exception {
        when(conteudoService.buscarPorCategoria(eq("Backend"), any()))
                .thenThrow(new RuntimeException("erro inesperado qualquer"));

        mockMvc.perform(get("/conteudo/categoria").param("categoria", "Backend"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"));
    }
}
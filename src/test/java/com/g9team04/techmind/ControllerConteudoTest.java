package com.g9team04.techmind;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g9team04.techmind.conteudo.ConteudoRequest;
import com.g9team04.techmind.conteudo.ConteudoResponse;
import com.g9team04.techmind.conteudo.ConteudoService;
import com.g9team04.techmind.conteudo.ControllerConteudo;
import com.g9team04.techmind.conteudo.LoteProcessor;
import com.g9team04.techmind.conteudo.LoteResponse;
import com.g9team04.techmind.infrastructure.ArquivoInvalidoException;
import com.g9team04.techmind.infrastructure.ConteudoNaoEncontradoException;
import com.g9team04.techmind.infrastructure.LoteProcessamentoException;
import com.g9team04.techmind.infrastructure.ValidatorCsv;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControllerConteudo.class)  // Carrega apenas o controller e seus dependentes web
public class ControllerConteudoTest {

    @Autowired
    private MockMvc mockMvc;                       // Agora será injetado corretamente

    private final ObjectMapper objectMapper = new ObjectMapper(); // Pode ser injetado com @Autowired, mas mantido manual

    @MockitoBean
    private ConteudoService conteudoService;      // Mock gerenciado pelo Spring

    @MockitoBean
    private LoteProcessor loteProcessor;

    @MockitoBean
    private ValidatorCsv validatorCsv;

    @Test
    void devePostarConteudoComSucesso() throws Exception {
        var request = new ConteudoRequest("Introdução ao Spring Boot", "Texto sobre Spring Boot.", null, null);
        var response = new ConteudoResponse(1L, "Introdução ao Spring Boot", "Backend", 0.95, List.of("Java", "Spring Boot"));

        when(conteudoService.processar(any(ConteudoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoria").value("Backend"))
                .andExpect(jsonPath("$.probabilidade").value(0.95))
                .andExpect(jsonPath("$.informacoes_adicionais[0]").value("Java"));
    }

    @Test
    void deveRetornar400QuandoTituloEstaAusente() throws Exception {
        var requestInvalido = new ConteudoRequest("", "Texto válido", null, null);

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldsErros.titulo").exists());
    }

    @Test
    void deveRetornar400QuandoTextoEstaAusente() throws Exception {
        var requestInvalido = new ConteudoRequest("Título válido", "", null, null);

        mockMvc.perform(post("/conteudo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldsErros.texto").exists());
    }

    @Test
    void deveBuscarPorCategoriaComSucesso() throws Exception {
        var response = new ConteudoResponse(1L, "titulo", "Backend", 0.9, List.of());
        var pagina = new PageImpl<>(List.of(response));

        when(conteudoService.buscarPorCategoria(anyString(), any())).thenReturn(pagina);

        mockMvc.perform(get("/conteudo/categoria").param("categoria", "Backend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoria").value("Backend"));
    }

    @Test
    void deveRetornar404QuandoCategoriaNaoExiste() throws Exception {
        when(conteudoService.buscarPorCategoria(anyString(), any()))
                .thenThrow(new ConteudoNaoEncontradoException("Inexistente"));

        mockMvc.perform(get("/conteudo/categoria").param("categoria", "Inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void deveRetornar404QuandoIdDeRelacionadosNaoExiste() throws Exception {
        when(conteudoService.buscarRelacionados(anyLong(), any()))
                .thenThrow(new ConteudoNaoEncontradoException(999L));

        mockMvc.perform(get("/conteudo/relacionados/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveProcessarLoteComSucesso() throws Exception {
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.csv", "text/csv", "titulo,texto\nA,B".getBytes());
        var loteResponse = new LoteResponse(1, 1, 0, List.of(1L), List.of());

        when(loteProcessor.processar(any(InputStream.class))).thenReturn(loteResponse);

        mockMvc.perform(multipart("/conteudo/lote").file(arquivo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sucessos").value(1))
                .andExpect(jsonPath("$.falhas").value(0));
    }

    @Test
    void deveRetornar400QuandoArquivoDeLoteEhInvalido() throws Exception {
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.txt", "text/plain", "conteudo qualquer".getBytes());

        doThrow(new ArquivoInvalidoException("Extensão de arquivo inválida. Esperado: .csv."))
                .when(validatorCsv).validarCsv(any());

        mockMvc.perform(multipart("/conteudo/lote").file(arquivo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void deveRetornar400QuandoLoteEstaTotalmenteFalho() throws Exception {
        var arquivo = new MockMultipartFile(
                "arquivo", "dados.csv", "text/csv", "titulo\nsomente uma coluna".getBytes());

        when(loteProcessor.processar(any(InputStream.class)))
                .thenThrow(new LoteProcessamentoException("Nenhuma linha do arquivo pôde ser processada."));

        mockMvc.perform(multipart("/conteudo/lote").file(arquivo))
                .andExpect(status().isBadRequest());
    }
}
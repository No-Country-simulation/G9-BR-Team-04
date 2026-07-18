package com.g9team04.techmind.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g9team04.techmind.infrastructure.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    // ##################### POST /users ##################### //

    @Test
    void devePostarUsuarioComSucesso() throws Exception {
        var request = new UserDtoRequest("arthur@techmind.com", "senha1234");
        var response = new UserDtoResponse(1L, "arthur@techmind.com");

        when(userService.createUser(any(UserDtoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("arthur@techmind.com"));
    }

    @Test
    void deveRetornar400QuandoEmailInvalido() throws Exception {
        var requestInvalido = new UserDtoRequest("emailinvalido", "senha1234");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoSenhaMenorQue8Caracteres() throws Exception {
        var requestInvalido = new UserDtoRequest("arthur@techmind.com", "123");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoEmailEstaEmBranco() throws Exception {
        var requestInvalido = new UserDtoRequest("", "senha1234");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409QuandoEmailJaExiste() throws Exception {
        var request = new UserDtoRequest("arthur@techmind.com", "senha1234");

        when(userService.createUser(any(UserDtoRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("arthur@techmind.com"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornar400QuandoSenhaInvalido() throws Exception {
        var requestInvalido = new UserDtoRequest("emailinvalido", "senha1234");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }


    // ################# GET /users/{id} #####################//

    @Test
    void deveBuscarUsuarioComSucesso() throws Exception {
        var response = new UserDtoResponse(1L, "arthur@techmind.com");

        when(userService.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("arthur@techmind.com"));

    }

    @Test
    void deveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new com.g9team04.techmind.infrastructure.UserNotFoundException(99L));
        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    // ######################## PUT /users/{id} ######################## //
    @Test
    void deveAtualizarSenhaComSucesso() throws Exception {
        var request = new UpdatePasswordDtoRequest("novaSenha1234");
        var response = new MessageDtoResponse("Senha alterada com sucesso");

        when(userService.updatePassword(eq(1L), any(UpdatePasswordDtoRequest.class))).thenReturn(response);
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Senha alterada com sucesso"));
    }

    @Test
    void deveRetornar404AoAtualizarSenhaDeUsuarioInexistente() throws Exception {
        var request = new UpdatePasswordDtoRequest("novaSenha123");

        when(userService.updatePassword(eq(99L), any(UpdatePasswordDtoRequest.class)))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(put("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400AoAtualizarComSenhaCurta() throws Exception {
        var requestInvalido = new UpdatePasswordDtoRequest("123");

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

}
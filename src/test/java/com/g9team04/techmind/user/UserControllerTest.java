package com.g9team04.techmind.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
                .andExpect(status().isBadRequest());
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
        when(userService.getUserById(99L)).thenThrow(new UserNotFoundException(99L));
        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    // ######################## PUT /users/{id} ######################## //
    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        var request = new UserDtoRequest("arthur@techmind.com", "novaSenha1234");
        var response = new UserDtoResponse(1L, "arthur@techmind.com");

       when(userService.updateUser(eq(1L), eq(request))).thenReturn(response);
       mockMvc.perform(put("/users/1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email").value("arthur@techmind.com"));
    }

    @Test
    void deveRetornar404AoAtualizarUsuarioInexistente() throws Exception {
        var request = new UserDtoRequest("novo@techmind.com", "novaSenha123");

        when(userService.updateUser(eq(99L), any(UserDtoRequest.class)))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(put("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400AoAtualizarComEmailInvalido() throws Exception {
        var requestInvalido = new UserDtoRequest("emailinvalido", "novaSenha123");

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    //#################### DELETE /users/{id} #########################
    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(1L);
    }

    @Test
    void deveRetornar404AoDeletarUsuarioInexistente() throws Exception {
        doThrow(new UserNotFoundException(99L)).when(userService).deleteUserById(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());
    }

}
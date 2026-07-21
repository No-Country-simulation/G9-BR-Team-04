package com.g9team04.techmind.user;

import com.g9team04.techmind.infrastructure.UserNotFoundException;
import com.g9team04.techmind.user.internal.UserEntity;
import com.g9team04.techmind.user.internal.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity("arthur@techmind.com", "senha1234");
    }

    // ##################### createUser ##################### //

    @Test
    void deveCriarUsuarioComSucesso() {
        var request = new UserDtoRequest("arthur@techmind.com", "senha1234");

        when(userRepository.existsByEmail("arthur@techmind.com")).thenReturn(false);

        var response = userService.createUser(request);

        assertThat(response.email()).isEqualTo("arthur@techmind.com");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void deveLancarExcecaoAoCriarUsuarioComEmailExistente() {
        var request = new UserDtoRequest("arthur@techmind.com", "senha1234");

        when(userRepository.existsByEmail("arthur@techmind.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("arthur@techmind.com");

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    // ##################### getUserById ##################### //

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var response = userService.getUserById(1L);

        assertThat(response.email()).isEqualTo("arthur@techmind.com");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoPorId() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .extracting(ex -> ((UserNotFoundException) ex).getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ##################### updateUser (senha) ##################### //

    @Test
    void deveAtualizarSenhaComSucesso() {
        var request = new UpdatePasswordDtoRequest("novaSenha1234");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var response = userService.updatePassword(1L, request);

        assertThat(response.message()).isEqualTo("Senha alterada com sucesso");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deveLancarExcecaoAoAtualizarSenhaDeUsuarioInexistente() {
        var request = new UpdatePasswordDtoRequest("novaSenha1234");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updatePassword(99L, request))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    // ##################### deleteUserById ##################### //

    @Test
    void deveDeletarUsuarioComSucesso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUserById(99L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).delete(any(UserEntity.class));
    }

    // ##################### getAllUsers ##################### //

    @Test
    void deveListarUsuariosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(page);

        var response = userService.getAllUsers(pageable);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void deveRetornarNoContentQuandoNaoHaUsuarios() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> paginaVazia = Page.empty();

        when(userRepository.findAll(pageable)).thenReturn(paginaVazia);

        var response = userService.getAllUsers(pageable);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
}
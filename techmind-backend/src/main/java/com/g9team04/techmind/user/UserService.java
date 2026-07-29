package com.g9team04.techmind.user;

import com.g9team04.techmind.infrastructure.UserNotFoundException;
import com.g9team04.techmind.user.internal.UserEntity;
import com.g9team04.techmind.user.internal.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        Optional.of(userDtoRequest.email())
                .filter(userRepository::existsByEmail)
                .ifPresent(email -> {
                    throw new EmailAlreadyExistsException(email);
                });

        var user = new UserEntity(userDtoRequest.email(), userDtoRequest.password());
        userRepository.save(user);
        return new UserDtoResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public UserDtoResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDtoResponse(user.getId(), user.getEmail()))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public MessageDtoResponse updatePassword(Long id, UpdatePasswordDtoRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(request.password());
                    userRepository.save(user);
                    return new MessageDtoResponse("Senha alterada com sucesso");
                })
                // 🌟 CORRIGIDO: Lança a sua exceção personalizada em vez do NoSuchElementException genérico
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        userRepository::delete,
                        () -> { throw new UserNotFoundException(id); }
                );
    }

    public ResponseEntity<List<UserDtoResponse>> getAllUsers(Pageable pageable) {
        return Optional.of(userRepository.findAll(pageable))
                .filter(page -> !page.isEmpty())
                .map(page -> page.stream()
                        .map(user -> new UserDtoResponse(user.getId(), user.getEmail()))
                        .toList()
                )
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
package com.g9team04.techmind.user;

import com.g9team04.techmind.user.internal.UserEntity;
import com.g9team04.techmind.user.internal.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        if (userRepository.existsByEmail(userDtoRequest.email())) {
            throw new EmailAlreadyExistsException(userDtoRequest.email());
        }

        var user = new UserEntity(userDtoRequest.email(), userDtoRequest.password());
        userRepository.save(user);

        return new UserDtoResponse(user.getId(), user.getEmail());
    }
    @Transactional
    public UserDtoResponse getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return new UserDtoResponse(user.getId(), user.getEmail());
    }
}

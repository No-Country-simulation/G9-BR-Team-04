package com.g9team04.techmind.user;

import com.g9team04.techmind.user.internal.UserEntity;
import com.g9team04.techmind.user.internal.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    @Transactional
    public UserDtoResponse updateUser(Long id, UserDtoRequest userDtoRequest) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getEmail().equals(userDtoRequest.email())) {
            userRepository.findByEmail(userDtoRequest.email())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(user.getId())) {
                            throw new EmailAlreadyExistsException(userDtoRequest.email());
                        }
                    });
        }


        user.setPassword(userDtoRequest.password());
        userRepository.save(user);

        return new UserDtoResponse(user.getId(), user.getEmail());

    }
    @Transactional
    public void deleteUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
        new UserDtoResponse(user.getId(), user.getEmail());
    }

}

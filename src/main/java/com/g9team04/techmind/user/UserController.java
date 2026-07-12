package com.g9team04.techmind.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> createUser( @Valid @RequestBody UserDtoRequest userDtoRequest) {

        var response = userService.createUser(userDtoRequest);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(response);
    }
    @GetMapping("/{id}")
    public UserDtoResponse getUserById(@PathVariable Long id) {

        return userService.getUserById(id) ;
    }
    @PutMapping("/{id}")
    public UserDtoResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserDtoRequest userDtoRequest) {
        return userService.updateUser(id, userDtoRequest);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}

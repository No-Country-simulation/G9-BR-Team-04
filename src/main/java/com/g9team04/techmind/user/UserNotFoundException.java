package com.g9team04.techmind.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(Long Id) {
        super("User with id " + Id + " not found");
    }
}

package com.g9team04.techmind.user;

public class UserErrorHandler extends RuntimeException {
    public UserErrorHandler(String message) {
        super(message);
    }
}

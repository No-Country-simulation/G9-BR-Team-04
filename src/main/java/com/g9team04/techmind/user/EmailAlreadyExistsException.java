package com.g9team04.techmind.user;

import com.g9team04.techmind.infrastructure.ApplicationException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApplicationException {

    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email, HttpStatus.CONFLICT);
    }
}


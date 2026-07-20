package com.g9team04.techmind.infrastructure;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final HttpStatus status;

    protected ApplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    protected ApplicationException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
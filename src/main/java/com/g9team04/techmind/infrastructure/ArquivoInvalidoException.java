package com.g9team04.techmind.infrastructure;
import org.springframework.http.HttpStatus;

public class ArquivoInvalidoException extends ApplicationException {

    public ArquivoInvalidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public ArquivoInvalidoException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
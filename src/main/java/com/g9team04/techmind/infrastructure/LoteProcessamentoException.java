package com.g9team04.techmind.infrastructure;

import org.springframework.http.HttpStatus;

public class LoteProcessamentoException extends ApplicationException {

    public LoteProcessamentoException(String message) {
        super(message, HttpStatus.BAD_REQUEST, null);
    }

    public LoteProcessamentoException(String message, Throwable cause ) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}

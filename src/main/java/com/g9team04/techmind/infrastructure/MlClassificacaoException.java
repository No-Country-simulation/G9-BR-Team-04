package com.g9team04.techmind.infrastructure;

import org.springframework.http.HttpStatus;

public class MlClassificacaoException extends ApplicationException {


    public MlClassificacaoException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }

    public MlClassificacaoException(String message, Throwable cause) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}

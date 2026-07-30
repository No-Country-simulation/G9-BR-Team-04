package com.g9team04.techmind.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDTO> handleApplicationException(ApplicationException e) {
        var errorResponse = new ErrorResponseDTO(e.getStatus().name(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException e) {
        var fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value",
                        (msg1, msg2) -> msg1
                ));
        var errorResponse = new ErrorResponseDTO("VALIDATION_ERROR", "Validation failed", Instant.now(), fieldErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        var errorResponse = new ErrorResponseDTO(
                "ARQUIVO_MUITO_GRANDE",
                "Arquivo excede o tamanho máximo permitido de 10MB."
        );
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpectedException(Exception e) {
        var errorResponse = new ErrorResponseDTO("INTERNAL_ERROR", "Unexpected error occurred");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}

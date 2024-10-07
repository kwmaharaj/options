package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@ControllerAdvice
public class CatGlobalExceptionHandler {

    // Handle specific exceptions
    @ExceptionHandler(CatOptionsException.class)
    public ResponseEntity<?> handleCatOptionsException(CatOptionsException ex, WebRequest request) {
        CatErrorDetails errorDetails = new CatErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false), createServerReference());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // General exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        CatErrorDetails errorDetails = new CatErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request.getDescription(false), createServerReference());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private UUID createServerReference(){
        return UUID.randomUUID();
    }
}

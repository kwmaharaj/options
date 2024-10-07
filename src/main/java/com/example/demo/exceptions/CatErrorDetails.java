package com.example.demo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatErrorDetails {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private String description;
    private HttpStatus status;
    private UUID serverReference;

    public CatErrorDetails(HttpStatus internalServerError, String message, String description, UUID serverReference) {
        this.status = internalServerError;
        this.message = message;
        this.description = description;
        this.serverReference = serverReference;
    }
}

package com.example.demo.utils;

import com.example.demo.exceptions.CatErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ResponseEntityUtils {

    public static <T> ResponseEntity<T> createExceptionResponseEntity(HttpStatus desiredHttpStatus, T responseBody, String message, String description, UUID serverReference) {
        // Handle errors
        if (desiredHttpStatus.isError()) {
            CatErrorDetails errorDetails = new CatErrorDetails(
                    desiredHttpStatus,
                    message,
                    description,
                    serverReference
            );

            // Generate the error JSON
            String errorJson = JsonUtils.safeSerializeErrorDetails(errorDetails, serverReference.toString());

            // Return the error JSON if it's an error response
            return new ResponseEntity<>((T) errorJson, desiredHttpStatus);
        }

        // Handle success
        return new ResponseEntity<>(responseBody, desiredHttpStatus);
    }
}

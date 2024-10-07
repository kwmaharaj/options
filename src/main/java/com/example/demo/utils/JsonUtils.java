package com.example.demo.utils;

import com.example.demo.exceptions.CatErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JsonUtils {

    public static String serializeObjectToJson(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Registering JavaTimeModule
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static String safeSerializeErrorDetails(CatErrorDetails errorDetails, String serverReference) {
        try {
            return serializeObjectToJson(errorDetails);
        } catch (Exception ex) {
            log.error("Failed to serialize error details: Server reference {} - Serialization error: {}", serverReference, ex.getMessage());
            return String.format(
                    "{\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":\"An unexpected error occurred\",\"serverReference\":\"%s\"}",
                    serverReference
            );
        }
    }

}

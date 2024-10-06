package com.example.demo.controller;

import com.example.demo.model.Cat;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Cats", description = "Endpoints for cats operations.")
@Slf4j
@RestController
@RequestMapping(value = "v1/" + "cats", produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
public class CatController {

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(summary = "Creates a cat.",responses = {@ApiResponse(responseCode = "200", description = "Cat created", content = @Content(schema = @Schema(implementation = Cat.class)))})
    @PostMapping
    public ResponseEntity<Cat> createSomething(){
        return ResponseEntity.ok(new Cat(UUID.randomUUID().toString(), "Created Black Cat"));
    }

    @Operation(summary = "Get a cat.", responses = {@ApiResponse(responseCode = "200", description = "Cat retrieved", content = @Content(schema = @Schema(implementation = Cat.class)))})
    @GetMapping
    public ResponseEntity<Cat> getSomething(){
        return ResponseEntity.ok(new Cat(UUID.randomUUID().toString(), "Get Black Cat"));
    }

    @Operation(summary = "Update a cat.", responses = {@ApiResponse(responseCode = "200", description = "Cat updated",content = @Content(schema = @Schema(implementation = Cat.class)))})
    @PutMapping
    public ResponseEntity<Cat> updateSomething(){
        return ResponseEntity.ok(new Cat(UUID.randomUUID().toString(), "Updated Black Cat."));
    }

    @Operation(summary = "Delete a cat.")
    @DeleteMapping
    public ResponseEntity<Cat> deleteSomething(){
        return ResponseEntity.ok(new Cat(UUID.randomUUID().toString(), "Deleted Black Cat."));
    }

    //TODO - See how to get springboot to recognise options, manually "recognising here"
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> requestOptions() throws Exception{

        //Headers available
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,PUT,DELETE,OPTIONS");


        // Fetch the schema for the Cat class and return
        Map<String, io.swagger.v3.oas.models.media.Schema> schemas = ModelConverters.getInstance().read(Cat.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String schemaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemas);


        return new ResponseEntity<>(schemaJson, headers, HttpStatus.OK);
    }

}

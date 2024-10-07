package com.example.demo.controller;

import com.example.demo.model.Cat;
import com.example.demo.service.CatService;
import com.example.demo.utils.ResponseEntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    final CatService catService;

    @Operation(summary = "Creates a cat.",responses = {@ApiResponse(responseCode = "200", description = "Cat created", content = @Content(schema = @Schema(implementation = Cat.class)))})
    @PostMapping("{name}")
    public ResponseEntity<Cat> createCat(@PathVariable String name){
        UUID serverReference = UUID.randomUUID();
        log.info("Creating cat named(%s), serverreference(%s)".formatted(name, serverReference));

        try {
            Cat cat = catService.createCat(name);
            return ResponseEntity.ok(cat);
        }catch(Exception e){
            // Error case, handle it by returning a detailed error response
            String message = "Failed to create cat named(%s)".formatted(name);
            String description = e.getMessage();
            log.error("Error creating cat named(%s), serverref(%s): %s".formatted(name, serverReference, e.getMessage()));

            // Using the generic error handler to return an error response
            return ResponseEntityUtils.createExceptionResponseEntity(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,  // For error, we don't need to pass a responseBody
                    message,
                    description,
                    serverReference
            );
        }
    }

    @Operation(summary = "Get a cat.", responses = {@ApiResponse(responseCode = "200", description = "Cat retrieved", content = @Content(schema = @Schema(implementation = Cat.class)))})
    @GetMapping("{id}")
    public ResponseEntity<Cat> getCatByID(@PathVariable UUID id){
        UUID serverReference = UUID.randomUUID();
        log.info("Getting cat with id(%s), serverref(%s)".formatted(id, serverReference));

        try {
            return ResponseEntity.ok(catService.getCatById(id));
        }catch(RuntimeException e){
            // Error case, handle it by returning a detailed error response
            String message = "Cat not found with ID: " + id;
            String description = e.getMessage();
            log.error("Error fetching cat with id(%s), serverref(%s), error(%s)".formatted(id, serverReference, e.getMessage()));

            // Using the generic error handler to return an error response
            return ResponseEntityUtils.createExceptionResponseEntity(
                    HttpStatus.NOT_FOUND,
                    null,  // For error, we don't need to pass a responseBody
                    message,
                    description,
                    serverReference
            );
        }
    }

    @Operation(summary = "Update a cat.", responses = {@ApiResponse(responseCode = "200", description = "Cat updated",content = @Content(schema = @Schema(implementation = Cat.class)))})
    @PutMapping
    public ResponseEntity<Cat> updateCat(@RequestBody Cat cat){
        UUID serverReference = UUID.randomUUID();
        log.info("updating cat with id(%s), serverref(%s)".formatted(cat.getId(), serverReference));

        try {
            Cat updated = catService.updateCat(cat);
            return ResponseEntity.ok(updated);
        }catch(RuntimeException e){
            // Error case, handle it by returning a detailed error response
            String message = "Failed to update cat(%s) ".formatted(cat.getId());
            String description = e.getMessage();
            log.error("Error updating cat with id(%s), serverref(%s), error(%s)".formatted(cat.getId(), serverReference, e.getMessage()));

            // Using the generic error handler to return an error response
            return ResponseEntityUtils.createExceptionResponseEntity(
                    HttpStatus.NOT_FOUND,
                    null,  // For error, we don't need to pass a responseBody
                    message,
                    description,
                    serverReference
            );
        }
    }

    @Operation(summary = "Delete a cat.")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCat(@PathVariable UUID id){
        UUID serverReference = UUID.randomUUID();
        log.info("deleting cat with id(%s)".formatted(id));

        try {
            return new ResponseEntity<>(catService.deleteCat(id), HttpStatus.OK);
        }catch(RuntimeException e){
            // Error case, handle it by returning a detailed error response
            String message = "Failed to delete cat(%s) ".formatted(id);
            String description = e.getMessage();
            log.error("Error delete cat with id(%s), serverref(%s), error(%s)".formatted(id, serverReference, e.getMessage()));

            // Using the generic error handler to return an error response
            return ResponseEntityUtils.createExceptionResponseEntity(
                    HttpStatus.NOT_FOUND,
                    null,  // For error, we don't need to pass a responseBody
                    message,
                    description,
                    serverReference
            );
        }
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> requestCatOptions(){
        UUID serverReference = UUID.randomUUID();//used to log a reference tied to the call. Will return to user, user may ask b.e to investigate and can do so finding the exception through serverreference.
        try {
            // Headers available
            HttpHeaders headers = new HttpHeaders();
            headers.add("Allow", "GET,POST,PUT,DELETE,OPTIONS");

            // Fetch the schema for the Cat class and return
            Map<String, io.swagger.v3.oas.models.media.Schema> schemas = ModelConverters.getInstance().read(Cat.class);
            ObjectMapper objectMapper = new ObjectMapper();
            String schemaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemas);
           return new ResponseEntity<>(schemaJson, headers, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntityUtils.createExceptionResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process OPTIONS request", "Error occurred while fetching schema for Cat class", "errordescriptino" , serverReference);
        }
    }

}

package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Cat {
    @Schema(description = "The unique ID of the cat", example = "2f9d54ec-3572-42d3-a1b1-d730fb0cb509")
    private String id;
    @Schema(description = "The name of the cat", example = "Black Cat")
    private String name;

    public Cat(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

package com.example.demo.model;

import com.example.demo.exceptions.CatErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto {

    private boolean isError;
    private CatErrorDetails errorDetails;

}

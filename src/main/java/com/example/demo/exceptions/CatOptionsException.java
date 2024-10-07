package com.example.demo.exceptions;

public class CatOptionsException extends RuntimeException{

    public CatOptionsException(String message){
        super(message);
    }

    public CatOptionsException(String message, Throwable cause){
        super(message, cause);
    }
}

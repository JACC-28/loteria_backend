package com.example.demo.exception;

public class TableroNoEncontradoException extends RuntimeException {

    public TableroNoEncontradoException(String message) {
        super(message);
    }

    public TableroNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
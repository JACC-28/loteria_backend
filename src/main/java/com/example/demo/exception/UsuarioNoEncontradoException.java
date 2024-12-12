package com.example.demo.exception; // Asegúrate de usar el paquete correcto

public class UsuarioNoEncontradoException extends RuntimeException { // Extiende RuntimeException

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }

    public UsuarioNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.example.demo.exception;

public class LoteriaException extends RuntimeException {

    public LoteriaException(String message) {
        super(message);
    }

    // Puedes añadir constructores adicionales para diferentes tipos de excepciones:
    public LoteriaException(String message, Throwable cause) {
        super(message, cause);
    }

    // Ejemplos de excepciones personalizadas:
    public static class UsuarioNoEncontradoException extends LoteriaException {
        public UsuarioNoEncontradoException(String username) {
            super("El usuario " + username + " no existe");
        }
    }

    public static class PartidaNoEncontradaException extends LoteriaException {
        public PartidaNoEncontradaException(Long partidaId) {
            super("La partida con ID " + partidaId + " no existe");
        }
    }

    public static class BoletoInvalidoException extends LoteriaException {
        public BoletoInvalidoException(String message) {
            super(message);
        }
    }


}
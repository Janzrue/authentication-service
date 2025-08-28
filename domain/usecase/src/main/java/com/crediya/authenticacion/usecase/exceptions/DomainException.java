package com.crediya.authenticacion.usecase.exceptions;

public abstract class DomainException extends RuntimeException {
    private final String code; // código único de error

    public DomainException(String code, String message) {
        super(message); // Mensaje descriptivo
        this.code = code; // Código que nos permitirá mapear en respuestas HTTP
    }
    public String getCode() {
        return code;
    }
}

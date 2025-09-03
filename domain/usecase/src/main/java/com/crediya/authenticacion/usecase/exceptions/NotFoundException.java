package com.crediya.authenticacion.usecase.exceptions;

/**
 * Excepción cuando no se encuentra un recurso (usuario, rol, etc.).
 * Se lanza cuando NO encontramos un recurso (ej: usuario o rol).
 */

public class NotFoundException extends DomainException {

    private static final String CODE = "NOT_FOUND"; // constante para evitar hardcodeo

    public NotFoundException(String message) {
        super(CODE, message);
    }
}

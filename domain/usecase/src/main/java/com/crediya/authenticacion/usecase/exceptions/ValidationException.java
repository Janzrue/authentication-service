package com.crediya.authenticacion.usecase.exceptions;

/**
 * Excepción para errores de validación (datos inválidos).
 * Se lanza cuando los datos proporcionados no cumplen con las reglas de validación.
 */
public class ValidationException extends DomainException {

    private static final String CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(CODE, message);
    }
}

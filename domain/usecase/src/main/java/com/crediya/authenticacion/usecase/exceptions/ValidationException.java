package com.crediya.authenticacion.usecase.exceptions;

import java.util.List;

public class ValidationException extends DomainException {

    private static final String CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(CODE, message);
    }
}

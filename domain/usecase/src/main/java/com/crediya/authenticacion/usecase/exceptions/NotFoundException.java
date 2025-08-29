package com.crediya.authenticacion.usecase.exceptions;

public class NotFoundException extends DomainException {

    private static final String CODE = "NOT_FOUND";

    public NotFoundException(String message) {
        super(CODE, message);
    }
}

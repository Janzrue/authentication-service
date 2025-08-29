package com.crediya.authenticacion.usecase.exceptions;

public class DuplicateException extends DomainException {
  private static final String CODE = "DUPLICATE_ERROR";

  public DuplicateException(String message) {
    super(CODE, message);
  }
}

package com.crediya.authenticacion.api.config;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String code;             // Código del error
    private String message;          // Mensaje general
    private List<String> errors;     // Lista de errores específicos (validaciones)
    private Instant timestamp;       // Momento del error
    private String path;             // Ruta donde ocurrió
}

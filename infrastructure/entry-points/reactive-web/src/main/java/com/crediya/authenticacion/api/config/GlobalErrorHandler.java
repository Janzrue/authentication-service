package com.crediya.authenticacion.api.config;


import com.crediya.authenticacion.usecase.exceptions.DomainException;
import com.crediya.authenticacion.usecase.exceptions.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
@Order(-2)
public class GlobalErrorHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse;

        if (ex instanceof ValidationException ve) {
            errorResponse = ErrorResponse.builder()
                    .code("VALIDATION_ERROR")
                    .message("Error de validación")
                    .errors(Collections.singletonList(ve.getMessage()))
                    .timestamp(Instant.now())
                    .path(exchange.getRequest().getPath().toString())
                    .build();

        } else if (ex instanceof DomainException de) {
            errorResponse = ErrorResponse.builder()
                    .code(de.getCode())
                    .message(de.getMessage())
                    .timestamp(Instant.now())
                    .path(exchange.getRequest().getPath().toString())
                    .build();

        } else {
            log.error("Error no controlado", ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = ErrorResponse.builder()
                    .code("INTERNAL_ERROR")
                    .message("Ha ocurrido un error inesperado")
                    .timestamp(Instant.now())
                    .path(exchange.getRequest().getPath().toString())
                    .build();
        }

        return writeResponse(exchange, status, errorResponse);
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, HttpStatus status, ErrorResponse errorResponse) {
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            var resp = exchange.getResponse();
            resp.setStatusCode(status);
            resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return resp.writeWith(Mono.just(resp.bufferFactory()
                    .wrap(json.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            log.error("Error generando JSON de respuesta", e);
            return Mono.empty();
        }
    }
}
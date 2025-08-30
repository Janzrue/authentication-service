package com.crediya.authenticacion.api.config;


import com.crediya.authenticacion.usecase.exceptions.DomainException;
import com.crediya.authenticacion.usecase.exceptions.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Order(-2) // antes que el DefaultErrorWebExceptionHandler
public class GlobalErrorHandler implements WebExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Object body;

        if (ex instanceof ValidationException ve) {
            body = Map.of("code", "VALIDATION_ERROR", "errors", List.of(ve.getMessage()));

        } else if (ex instanceof DomainException de) {
            body = Map.of("code", de.getCode(), "message", de.getMessage());
        } else {
            log.error("Uncontrolled error", ex);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = Map.of("code", "INTERNAL_ERROR", "message", "An error has occurred");
        }

        try {
            String json = objectMapper.writeValueAsString(body);
            var resp = exchange.getResponse();
            resp.setStatusCode(status);
            resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return resp.writeWith(Mono.just(resp.bufferFactory()
                    .wrap(json.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            log.error("Error generating JSON response", e);
            return Mono.empty();
        }
    }
}

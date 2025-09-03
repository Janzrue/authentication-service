package com.crediya.authenticacion.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class RequestLoggingFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        log.info("REQ {} {}", req.getMethod(), req.getURI().getPath());
        return chain.filter(exchange)
                .doOnSuccess(v -> log.info("RES {}", exchange.getResponse().getStatusCode()));
    }
}

package com.crediya.authenticacion.model.auth.gateways;

import reactor.core.publisher.Mono;

public interface PasswordEncoderPort {
    Mono<String> encode(String raw);
    Mono<Boolean> matches(String raw, String encoded);
}

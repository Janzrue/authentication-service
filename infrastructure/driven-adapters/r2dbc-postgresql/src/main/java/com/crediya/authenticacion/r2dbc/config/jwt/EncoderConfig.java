package com.crediya.authenticacion.r2dbc.config.jwt;

import com.crediya.authenticacion.model.auth.gateways.PasswordEncoderPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

@Configuration
public class EncoderConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoderPort passwordEncoderPort(BCryptPasswordEncoder encoder) {
        return new PasswordEncoderPort() {
            @Override
            public Mono<String> encode(String raw) {
                return Mono.fromCallable(() -> encoder.encode(raw));
            }

            @Override
            public Mono<Boolean> matches(String raw, String encoded) {
                return Mono.fromCallable(() -> encoder.matches(raw, encoded));
            }
        };
    }
}

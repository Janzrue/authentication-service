package com.crediya.authenticacion.api.config.jwt;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {
    public JwtAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager,
                                      BearerTokenServerAuthenticationConverter converter) {
        super(authenticationManager);
        setServerAuthenticationConverter(converter);
    }
}

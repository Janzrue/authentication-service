package com.crediya.authenticacion.api.config.jwt;

import com.crediya.authenticacion.model.auth.AuthConstants;
import com.crediya.authenticacion.model.tokeninfo.gateways.TokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenProvider tokenProvider;

    public JwtReactiveAuthenticationManager(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        return tokenProvider.parseAndValidate(token)
                .map(claims -> {
                    Object rolesObj = claims.get(AuthConstants.CLAIM_ROLES);
                    List<String> roles = rolesObj instanceof List ? (List<String>) rolesObj : List.of();

                    var authorities = roles.stream()
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                            .collect(Collectors.toList());

                    String rawPrincipal = (String) claims.get(AuthConstants.CLAIM_EMAIL);
                    if (rawPrincipal == null) {
                        rawPrincipal = (String) ((Map<String, Object>) claims).get(Claims.SUBJECT);
                    }
                    final String principal = rawPrincipal;

                    Authentication auth = new AbstractAuthenticationToken(authorities) {
                        @Override
                        public Object getCredentials() {
                            return token;
                        }

                        @Override
                        public Object getPrincipal() {
                            return principal;
                        }
                    };
                    ((AbstractAuthenticationToken) auth).setAuthenticated(true);
                    return auth;
                })
                .cast(Authentication.class) //  Esto hace que el tipo sea correcto
                .onErrorMap(e -> new BadCredentialsException("Token inválido", e));
    }
}

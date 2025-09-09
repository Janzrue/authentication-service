package com.crediya.authenticacion.api.config.jwt;

import com.crediya.authenticacion.model.auth.AuthConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            JwtAuthenticationWebFilter jwtFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(AuthConstants.LOGIN_PATH, AuthConstants.REFRESH_PATH,
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()

                        //.pathMatchers(HttpMethod.POST, "/api/v1/users").permitAll()

                        .pathMatchers(HttpMethod.GET, "/api/v1/users/**")
                        .hasAnyRole(AuthConstants.ROLE_ADMIN, AuthConstants.ROLE_ASESOR, AuthConstants.ROLE_CLIENTE)

                        .pathMatchers("/api/v1/users/**")
                        .hasAnyRole(AuthConstants.ROLE_ADMIN, AuthConstants.ROLE_ASESOR)

                        .pathMatchers(HttpMethod.GET, "/api/v1/applications")
                        .hasRole(AuthConstants.ROLE_ASESOR)

                        .pathMatchers(HttpMethod.POST, "/api/v1/applications")
                        .hasRole(AuthConstants.ROLE_CLIENTE)

                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}

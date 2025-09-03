package com.crediya.authenticacion.api;



import com.crediya.authenticacion.api.dto.LoginDTO;
import com.crediya.authenticacion.api.dto.RefreshDTO;
import com.crediya.authenticacion.api.dto.TokenDTO;
import com.crediya.authenticacion.model.auth.AuthCredentials;
import com.crediya.authenticacion.model.tokeninfo.TokenInfo;
import com.crediya.authenticacion.usecase.auth.AuthUseCase;
import com.crediya.authenticacion.usecase.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthUseCase authUseCase;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginDTO.class)
                .switchIfEmpty(Mono.error(new ValidationException("Body requerido")))
                .flatMap(body -> authUseCase.login(AuthCredentials.builder()
                        .email(body.getEmail())
                        .password(body.getPassword())
                        .build()))
                .map(this::toResponse)
                .flatMap(resp -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(resp))
                .doOnSuccess(r -> log.info("Login exitoso"))
                .onErrorResume(ErrorResponses::toResponse);
    }

    public Mono<ServerResponse> refresh(ServerRequest request) {
        return request.bodyToMono(RefreshDTO.class)
                .switchIfEmpty(Mono.error(new ValidationException("Body requerido")))
                .flatMap(body -> authUseCase.refresh(body.getRefreshToken()))
                .map(this::toResponse)
                .flatMap(resp -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(resp))
                .doOnSuccess(r -> log.info("Refresh exitoso"))
                .onErrorResume(ErrorResponses::toResponse);
    }

    private TokenDTO toResponse(TokenInfo t) {
        return TokenDTO.builder()
                .tokenType(t.getTokenType())
                .accessToken(t.getAccessToken())
                .refreshToken(t.getRefreshToken())
                .expiresIn(t.getExpiresIn())
                .build();
    }
}

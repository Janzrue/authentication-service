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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthUseCase authUseCase;

    /**
     * Login de usuario.
     * Recibe un LoginRequest, valida que no esté vacío y llama al caso de uso.
     * Luego convierte el resultado a TokenResponse y retorna un ServerResponse.
     */
    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginDTO.class) // Lee el cuerpo de la petición como LoginRequest
                .switchIfEmpty(Mono.error(new ValidationException("Body requerido")))
                .flatMap(body -> authUseCase.login(AuthCredentials.builder()
                        .email(body.getEmail())
                        .password(body.getPassword())
                        .build()))
                // Llama al caso de uso, devuelve Mono<TokenInfo>
                .map(this::toResponse)// Convierte TokenInfo a TokenResponse (DTO para API)
                .flatMap(resp -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(resp)) // Construye el ServerResponse con JSON
                .doOnSuccess(r -> log.info("Login exitoso"))
                .onErrorResume(ErrorResponses::toResponse); // Manejo centralizado de errores
    }

    /**
     * Refresh de token.
     * Recibe un RefreshRequest, valida y llama al caso de uso de refresh.
     * Retorna el TokenResponse actualizado.
     */
    public Mono<ServerResponse> refresh(ServerRequest request) {
        return request.bodyToMono(RefreshDTO.class) // Lee body como RefreshRequest
                .switchIfEmpty(Mono.error(new ValidationException("Body requerido"))) // Valida que exista body
                .flatMap(body -> authUseCase.refresh(body.getRefreshToken())) // Llama al caso de uso refresh
                .map(this::toResponse) // Convierte TokenInfo a TokenResponse
                .flatMap(resp -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(resp))// Construye ServerResponse con JSON
                .doOnSuccess(r -> log.info("Refresh exitoso"))
                .onErrorResume(ErrorResponses::toResponse); // Manejo de errores centralizado
    }

    /**
     * Convierte TokenInfo del caso de uso a TokenResponse DTO de API.
     * @param t TokenInfo
     * @return TokenResponse
     */
    private TokenDTO toResponse(TokenInfo t) {
        return TokenDTO.builder()
                .tokenType(t.getTokenType())
                .accessToken(t.getAccessToken())
                .refreshToken(t.getRefreshToken())
                .expiresIn(t.getExpiresIn())
                .build();
    }
}

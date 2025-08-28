package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.SaveUserDTO;
import com.crediya.authenticacion.api.mapper.UserApiMapper;
import com.crediya.authenticacion.usecase.exceptions.ValidationException;
import com.crediya.authenticacion.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);
    private final UserUseCase userUseCase;
    private final UserApiMapper userApiMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest request) {
        return request.bodyToMono(SaveUserDTO.class)
                .map(dto -> {
                    log.info("User registration request: {}", dto.getEmail());
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        List<String> errs = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.toList());
                        throw new ValidationException(errs);
                    }
                    return userApiMapper.toDomain(dto);
                })
                .flatMap(user -> {
                    return userUseCase.saveUser(user);
                }) // devuelve Mono<User>
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser)) // aquí cambia el tipo final a Mono<ServerResponse>
                .onErrorResume(e -> {
                    log.error("Error saving user: {}", e.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("{\"error\":\"" + e.getMessage() + "\"}");
                });
    }
}

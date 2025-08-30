package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.UserDTO;
import com.crediya.authenticacion.api.mapper.UserApiMapper;
import com.crediya.authenticacion.usecase.exceptions.DuplicateException;
import com.crediya.authenticacion.usecase.exceptions.NotFoundException;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);
    private final UserUseCase userUseCase;
    private final UserApiMapper userApiMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest request) {
        return request.bodyToMono(UserDTO.class)
                .flatMap(dto -> {
                    // 1️⃣ Validaciones básicas
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        String errs = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        return Mono.error(new ValidationException(errs));
                    }

                    // 2️⃣ Validación de email único
                    return userUseCase.existsByEmail(dto.getEmail())
                            .flatMap(emailExists -> {
                                if (emailExists) {
                                    return Mono.error(new ValidationException("The email is already registered."));
                                }
                                return Mono.just(dto);
                            });
                })
                .flatMap(dto -> {
                    // 3️⃣ Validación de documento único
                    return userUseCase.existsByIdentificationNumber(dto.getIdentificationNumber())
                            .flatMap(docExists -> {
                                if (docExists) {
                                    return Mono.error(new ValidationException("The identification number is already registered."));
                                }
                                return Mono.just(dto);
                            });
                })
                .map(userApiMapper::toDomain)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userApiMapper.toDTO(savedUser)))
                .onErrorResume(e -> {
                    if (e instanceof ValidationException || e instanceof DuplicateException) {
                        log.warn("Error saving user", e);

                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{\"error\":\"" + e.getMessage() + "\"}");
                    }
                    return ServerResponse.status(500)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("{\"error\":\"Internal server error\"}");
                });

    }

    // Obtener todos los usuarios
    public Mono<ServerResponse> listenFindAllUsers(ServerRequest request) {
        Flux<UserDTO> users = userUseCase.findAllUsers()
                .map(userApiMapper::toDTO);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, UserDTO.class);
    }

    // Obtener usuario por ID
    public Mono<ServerResponse> listenFindUserByIdNumber(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return userUseCase.findUserByIdNumber(id)
                .map(userApiMapper::toDTO)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .onErrorResume(NotFoundException.class,
                        e -> ServerResponse.notFound().build());
    }

    // Editar usuario
    public Mono<ServerResponse> listenEditUser(ServerRequest request) {
        return request.bodyToMono(UserDTO.class)
                .map(userApiMapper::toDomain)
                .flatMap(userUseCase::editUser)
                .map(userApiMapper::toDTO)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .onErrorResume(NotFoundException.class,
                        e -> ServerResponse.notFound().build());
    }

    // Eliminar usuario
    public Mono<ServerResponse> listenDeleteUser(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return userUseCase.deleteUser(id)
                .then(ServerResponse.noContent().build())
                .onErrorResume(NotFoundException.class,
                        e -> ServerResponse.notFound().build());
    }

    // Verificar si email existe
    public Mono<ServerResponse> existsByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return userUseCase.existsByEmail(email)
                .flatMap(exists -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"exists\": " + exists + "}"));
    }

    // Verificar si documento existe
    public Mono<ServerResponse> existsByIdentificationNumber(ServerRequest request) {
        String doc = request.pathVariable("idNumber");
        return userUseCase.existsByIdentificationNumber(doc)
                .flatMap(exists -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"exists\": " + exists + "}"));
    }
}

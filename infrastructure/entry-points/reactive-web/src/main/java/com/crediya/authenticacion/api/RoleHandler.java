package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.RoleDTO;
import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.usecase.role.RoleUseCase;
import com.crediya.authenticacion.usecase.exceptions.ValidationException;
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
public class RoleHandler {

    private static final Logger log = LoggerFactory.getLogger(RoleHandler.class);

    private final RoleUseCase roleUseCase;
    private final Validator validator;


    public Mono<ServerResponse> listenSaveRole(ServerRequest request) {
        return request.bodyToMono(RoleDTO.class)
                .map(dto -> {
                    log.info("Request to create role: {}", dto.getName());
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        String errs = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        throw new ValidationException(errs); // usamos  ValidationException
                    }
                    return Role.builder()
//                            .id(dto.getUniqueId())
                            .name(dto.getName())
                            .description(dto.getDescription())
                            .build();
                })
                .flatMap(roleUseCase::saveRole)
                .flatMap(savedRol -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedRol))
                .onErrorResume(e -> {
                    log.error("Error creating role: {}", e.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("{\"error\":\"" + e.getMessage() + "\"}");
                });
    }

    public Mono<ServerResponse> listenFindRoleById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("uniqueId"));
        log.info("Checking role with ID: {}", id);
        return roleUseCase.findRoleById(id)
                .flatMap(role -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(role))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenUpdateRole(ServerRequest request) {
        return request.bodyToMono(RoleDTO.class)
                .map(this::validateDto)
                .flatMap(roleUseCase::updateRole)
                .flatMap(updatedRole -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedRole))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> listenDeleteRole(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("uniqueId"));
        return roleUseCase.deleteRole(id)
                .then(ServerResponse.noContent().build())
                .onErrorResume(this::handleError);
    }

    private Role validateDto(RoleDTO dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errs = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errs);
        }
        return Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    private Mono<ServerResponse> handleError(Throwable e) {
        log.error("Error: {}", e.getMessage());
        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"error\":\"" + e.getMessage() + "\"}");
    }
}

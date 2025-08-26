package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.SaveUserDTO;
import com.crediya.authenticacion.api.mapper.UserDTOMapper;
import com.crediya.authenticacion.model.User;
import com.crediya.authenticacion.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final RequestValidator validator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SaveUserDTO.class)
                .flatMap(validator::validate)
                .map(userDTOMapper::toModel)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser))
                .onErrorResume(ConstraintViolationException.class, ex ->
                        ServerResponse.badRequest()
                                .bodyValue(createErrorResponse(ex))
                );
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                //.contentType(MediaType.APPLICATION_JSON)
                //.contentType(MediaType.APPLICATION_NDJSON)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userUseCase.getAllUsers(), User.class);
    }

    private Object createErrorResponse(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(violation -> String.format("%s: %s", violation.getPropertyPath(), "fallando"))
                .toList();
    }
}

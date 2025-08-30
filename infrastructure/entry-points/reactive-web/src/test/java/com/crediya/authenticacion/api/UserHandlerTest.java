package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.UserDTO;
import com.crediya.authenticacion.api.mapper.UserApiMapper;
import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.usecase.user.UserUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

class UserHandlerTest {
    private WebTestClient webTestClient;
    private UserUseCase userUseCase;
    private UserApiMapper userApiMapper;
    private Validator validator;

    @BeforeEach
    void setup() {
        // Mocks
        userUseCase = mock(UserUseCase.class);
        userApiMapper = mock(UserApiMapper.class);
        validator = mock(Validator.class);

        // Handler real con mocks
        UserHandler handler = new UserHandler(userUseCase, userApiMapper, validator);

        // Router mínimo para pruebas
        RouterFunction<ServerResponse> router = route()
                .POST("/api/v1/users", handler::listenSaveUser)
                .build();

        // WebTestClient ligado al router
        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    @Test
    void registrarUsuario_success() {
        // DTO de prueba
        UserDTO dto = new UserDTO();
        dto.setName("Juan");
        dto.setLastName("Perez");
        dto.setEmail("juan@example.com");
        dto.setIdentificationNumber("12345678");

        // Domain User
        User user = new User();
        user.setEmail("juan@example.com");

        // Comportamiento de mocks
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        when(userUseCase.existsByEmail(dto.getEmail())).thenReturn(Mono.just(false));
        when(userUseCase.existsByIdentificationNumber(dto.getIdentificationNumber())).thenReturn(Mono.just(false));
        when(userApiMapper.toDomain(dto)).thenReturn(user);
        when(userUseCase.saveUser(user)).thenReturn(Mono.just(user));
        when(userApiMapper.toDTO(user)).thenReturn(dto);

        // Test
        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Juan")
                .jsonPath("$.lastName").isEqualTo("Perez");
    }

    @Test
    void registrarUsuario_validationError() {
        UserDTO dto = new UserDTO(); // vacío

        // Mock validator para que devuelva error
        var violation = mock(jakarta.validation.ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Required field");
        when(validator.validate(dto)).thenReturn(Set.of(violation));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists();
    }
}

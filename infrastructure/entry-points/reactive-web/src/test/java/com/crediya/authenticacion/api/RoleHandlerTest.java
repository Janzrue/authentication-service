package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.RoleDTO;
import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.usecase.role.RoleUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class RoleHandlerTest {


    private WebTestClient webTestClient;

    private RoleUseCase roleUseCase;
    private Validator validator;

    @BeforeEach
    void setup() {
        // Inicializamos los mocks
        roleUseCase = Mockito.mock(RoleUseCase.class);
        validator = Mockito.mock(Validator.class);

        // Creamos el handler real con los mocks
        RoleHandler roleHandler = new RoleHandler(roleUseCase, validator);

        // Router mínimo para pruebas
        RouterFunction<ServerResponse> router = route()
                .POST("/api/v1/roles", roleHandler::listenSaveRole)
                .build();

        // Ligamos WebTestClient al router
        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    /** Helper para crear un DTO de rol de prueba */
    private RoleDTO createTestRoleDTO() {

        RoleDTO dto = new RoleDTO();
        dto.setName("ADMIN");
        dto.setDescription("Rol de prueba");
        return dto;
    }

    @Test
    void saveRole_success() {
        RoleDTO dto = createTestRoleDTO();

        // Creamos un Role simulado que debe devolver el use case
        Role role = Role.builder()
                .id(1L)
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        // Mock del comportamiento del use case
        when(roleUseCase.saveRole(any(Role.class))).thenReturn(Mono.just(role));

        // Mock validator: no hay errores de validación
        when(validator.validate(any(RoleDTO.class))).thenReturn(java.util.Collections.emptySet());

        // Ejecutamos la prueba
        webTestClient.post()
                .uri("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectStatus().isCreated() // Esperamos 201 Created
                .expectBody()
                .jsonPath("$.name").isEqualTo("ADMIN")
                .jsonPath("$.description").isEqualTo("Rol de prueba");
    }

    @Test
    void saveRole_validationError() {
        RoleDTO dto = new RoleDTO(); // nombre null => error de validación

        jakarta.validation.ConstraintViolation<RoleDTO> violation = Mockito.mock(jakarta.validation.ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Campo requerido");
        when(validator.validate(any(RoleDTO.class))).thenReturn(java.util.Set.of(violation));

        webTestClient.post()
                .uri("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists();
    }
}

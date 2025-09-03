package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.RoleDTO;
import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.usecase.role.RoleUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;@WebFluxTest

@ContextConfiguration(classes = {RoleRouterRest.class, RoleHandler.class})
public class RoleHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RoleUseCase roleUseCase;

    @Test
    void saveRole_success() {
        Role role = Role.builder().id(1L).name("ADMIN").description("Rol de prueba").build();
        when(roleUseCase.saveRole(any(Role.class))).thenReturn(Mono.just(role));

        RoleDTO dto = new RoleDTO();
        dto.setName("ADMIN");
        dto.setDescription("Rol de prueba");

        webTestClient.post()
                .uri("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("ADMIN")
                .jsonPath("$.description").isEqualTo("Rol de prueba");
    }

    @Test
    void saveRole_validationError() {
        RoleDTO dto = new RoleDTO(); // nombre null => error de validación

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

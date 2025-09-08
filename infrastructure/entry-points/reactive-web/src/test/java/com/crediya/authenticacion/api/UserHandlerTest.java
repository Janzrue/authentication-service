package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.UserDTO;
import com.crediya.authenticacion.api.mapper.UserApiMapper;
import com.crediya.authenticacion.model.auth.gateways.PasswordEncoderPort;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

class UserHandlerTest {
    private WebTestClient webTestClient;
    private UserUseCase userUseCase;
    private UserApiMapper userApiMapper;
    private Validator validator;
    private PasswordEncoderPort passwordEncoder;

    @BeforeEach
    void setup() {
        // Mocks
        userUseCase = mock(UserUseCase.class);
        userApiMapper = mock(UserApiMapper.class);
        validator = mock(Validator.class);
        passwordEncoder = mock(PasswordEncoderPort.class);

        // Simula que el password encoder devuelve un hash
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->
                Mono.just("hashed-" + invocation.getArgument(0)));

        // Mapper seguro: convierte DTO -> Domain
        when(userApiMapper.toDomain(any(UserDTO.class))).thenAnswer(invocation -> {
           UserDTO dto = invocation.getArgument(0);
           return User.builder()
                   .name(dto.getName())
                   .lastName(dto.getLastName())
                   .email(dto.getEmail())
                   .identificationNumber(dto.getIdentificationNumber())
                   .birthDate(dto.getBirthDate())
                   .phone(dto.getPhone())
                   .baseSalary(dto.getBaseSalary() != null ? dto.getBaseSalary() : null)
                   .address(dto.getAddress())
                   .idRole(dto.getRoleId() != null ? BigDecimal.valueOf(dto.getRoleId()) : null)
                   .password(dto.getPassword())
                   .build();
        });

        // Mapper seguro: convierte DTO -> Domain
        when(userApiMapper.toDTO(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            UserDTO  dto = new UserDTO();
            dto.setName(user.getName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setIdentificationNumber(user.getIdentificationNumber());
            dto.setBirthDate(user.getBirthDate());
            dto.setPhone(user.getPhone());
            dto.setBaseSalary(user.getBaseSalary() != null ? user.getBaseSalary() : null);
            dto.setAddress(user.getAddress());
            dto.setRoleId(user.getIdRole() != null ? user.getIdRole().longValue() : null);
            dto.setPassword(user.getPassword());
            return dto;
        });

        // Handler real con los mocks
        UserHandler handler = new UserHandler(userUseCase, userApiMapper, validator, passwordEncoder);

        // Router mínimo para pruebas
        RouterFunction<ServerResponse> router = route()
                .POST("/api/v1/users", handler::listenSaveUser)
                .build();

        // WebTestClient ligado al router
        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    // Helper: crear un DTO de prueba
    private UserDTO createTestUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setName("Juan");
        dto.setLastName("Perez");
        dto.setEmail("juan@example.com");
        dto.setIdentificationNumber("12345678");
        dto.setBirthDate(LocalDate.parse("1990-01-01"));
        dto.setPhone("3001234567");
        dto.setBaseSalary(2000);
        dto.setAddress("Calle 1");
        dto.setRoleId(1L);
        dto.setPassword("123456"); // obligatorio
        return dto;
    }

    // Helper: crear el User de dominio correspondiente
    private User createTestUserDomain(UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .identificationNumber(dto.getIdentificationNumber())
                .birthDate(dto.getBirthDate())
                .phone(dto.getPhone())
                .baseSalary(dto.getBaseSalary())
                .address(dto.getAddress())
                .idRole(BigDecimal.valueOf(dto.getRoleId()))
                .password(dto.getPassword())
                .build();
    }

    @Test
    void registrarUsuario_success() {
        UserDTO dto = createTestUserDTO();
        User userDomain = createTestUserDomain(dto);

        // El validador no retorna errores
        when(validator.validate(any(UserDTO.class))).thenReturn(Collections.emptySet());
        // El email y documento no existen
        when(userUseCase.existsByEmail(dto.getEmail())).thenReturn(Mono.just(false));
        when(userUseCase.existsByIdentificationNumber(dto.getIdentificationNumber())).thenReturn(Mono.just(false));
        // Guardar usuario retorna el usuario de dominio
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(userDomain));

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(dto.getName())
                .jsonPath("$.email").isEqualTo(dto.getEmail())
                .jsonPath("$.password").value(pw -> ((String) pw).startsWith("hashed-"));
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

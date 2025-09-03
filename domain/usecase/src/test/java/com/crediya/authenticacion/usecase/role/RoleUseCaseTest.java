package com.crediya.authenticacion.usecase.role;

import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

public class RoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository; //  Mock: simulamos el repositorio (no conecta a DB).

    @InjectMocks
    private RoleUseCase roleUseCase; //  Clase que probamos (se inyecta el mock del repo).

    private Role role; //  Objeto que usaremos en pruebas.

    @BeforeEach
    void setUp() {
        //  Inicializa los mocks de Mockito antes de cada test.
        MockitoAnnotations.openMocks(this);

        //  Creamos un rol de ejemplo para pruebas.
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
    }

    @Test
    void saveRole_ok() {
        //  Simulamos que el repo guarda el rol y retorna el mismo objeto.
        when(roleRepository.saveRole(role)).thenReturn(Mono.just(role));

        //  Verificamos con StepVerifier que el flujo retorna ese rol.
        StepVerifier.create(roleUseCase.saveRole(role))
                .expectNext(role) //  Esperamos que salga el mismo rol
                .verifyComplete(); //  Y que finalice sin error.
    }

    @Test
    void findRole_ok() {
        //  Simulamos que el repo encuentra el rol por ID.
        when(roleRepository.findRoleById(1L)).thenReturn(Mono.just(role));

        StepVerifier.create(roleUseCase.findRoleById(1L))
                .expectNext(role) //  Esperamos ese rol
                .verifyComplete();
    }

    @Test
    void updateRole_ok() {
        //  Simulamos actualización (repo retorna el mismo rol actualizado).
        when(roleRepository.updateRole(role)).thenReturn(Mono.just(role));

        StepVerifier.create(roleUseCase.updateRole(role))
                .expectNext(role)
                .verifyComplete();
    }

    @Test
    void deleteRole_ok() {
        //  Simulamos que al eliminar retorna vacío (Mono.empty()).
        when(roleRepository.deleteRole(1L)).thenReturn(Mono.empty());

        StepVerifier.create(roleUseCase.deleteRole(1L))
                .verifyComplete(); //  Esperamos que termine sin error.
    }

}

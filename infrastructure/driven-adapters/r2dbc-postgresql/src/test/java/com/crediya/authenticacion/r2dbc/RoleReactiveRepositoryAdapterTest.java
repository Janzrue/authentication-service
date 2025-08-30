package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.r2dbc.entity.RoleEntity;
import com.crediya.authenticacion.r2dbc.mapper.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class       RoleReactiveRepositoryAdapterTest {

    @Mock
    private RoleReactiveRepository repository; // Mock del repositorio reactivo

    @Mock
    private RoleMapper mapper; // Mock del mapper Rol <-> RoleEntity

    @InjectMocks
    private RoleReactiveRepositoryAdapter adapter; // Adapter a probar

    private Role rol;
    private RoleEntity entity;

    @BeforeEach
    void setUp() {
        // Creamos un rol de ejemplo
        rol = Role.builder()
                .uniqueId(1)
                .name("ADMIN")
                .description("Administrador del sistema")
                .build();

        // Creamos la entidad correspondiente
        entity = RoleEntity.builder()
                .idRole(1)
                .name("ADMIN")
                .description("Administrador del sistema")
                .build();
    }

    @Test
    void findById_shouldReturnRol() {
        // Configuramos comportamiento de los mocks
        when(repository.findById(1)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(rol);

        // Ejecutamos método
        StepVerifier.create(adapter.findById(1))
                .expectNext(rol)
                .verifyComplete();

        verify(repository, times(1)).findById(1);
    }

    @Test
    void save_shouldMapAndCallRepo() {
        when(mapper.toEntity(rol)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(rol);

        StepVerifier.create(adapter.save(rol))
                .expectNext(rol)
                .verifyComplete();

        verify(repository, times(1)).save(entity);
    }

    @Test
    void update_shouldModifyExistingRol() {
        Role updatedRole = Role.builder()
                .uniqueId(1)
                .name("SUPER_ADMIN")
                .description("Administrador principal")
                .build();

        RoleEntity updatedEntity = RoleEntity.builder()
                .idRole(1)
                .name("SUPER_ADMIN")
                .description("Administrador principal")
                .build();

        when(repository.findById(1)).thenReturn(Mono.just(entity)); // encuentra rol existente
        when(mapper.toEntity(updatedRole)).thenReturn(updatedEntity);
        when(repository.save(updatedEntity)).thenReturn(Mono.just(updatedEntity));
        when(mapper.toModel(updatedEntity)).thenReturn(updatedRole);

        StepVerifier.create(adapter.save(updatedRole))
                .expectNext(updatedRole)
                .verifyComplete();

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(updatedEntity);
    }

    @Test
    void delete_shouldCallRepo() {
        when(repository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteRole(1L))
                .verifyComplete();

        verify(repository, times(1)).deleteById(1);
    }
}

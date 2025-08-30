package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import com.crediya.authenticacion.r2dbc.mapper.UserR2dbcMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository repo;

    @Mock
    private UserR2dbcMapper mapper;

    @Mock
    private TransactionalOperator tx;

    @InjectMocks
    private UserReactiveRepositoryAdapter adapter;

    private User user;
    private UserEntity entity;

    @BeforeEach
    void setUp() {
        // Creamos un usuario y su entidad equivalente
        user = new User(1L, "Ana", "Martinez", LocalDate.parse("1990-01-01"), "Calle 1", "3000000", "ana@test.com",
                2000, "123", BigDecimal.valueOf(1));

        entity = new UserEntity(1L, "Ana", "Martinez", LocalDate.parse("1990-01-01"), "Calle 1", "3000000",
                "ana@test.com", 2000, "123", 1L);
    }

    @Test
    void saveUser_shouldMapAndCallRepo() {
        // Configuramos comportamiento de mocks
        when(mapper.toEntity(user)).thenReturn(entity); // Mapper transforma User -> UserEntity
        when(repo.save(entity)).thenReturn(Mono.just(entity)); // Repositorio guarda y retorna entidad
        when(mapper.toModel(entity)).thenReturn(user); // Mapper transforma de vuelta a User
        when(tx.transactional(any(Mono.class))).thenAnswer(i -> i.getArgument(0)); // Simula transacción

        // Ejecutamos el método a testear
        StepVerifier.create(adapter.saveUser(user))
                .expectNext(user) // Esperamos que devuelva el mismo usuario
                .verifyComplete();

        // Verificamos que el repositorio se llamó correctamente
        verify(repo, times(1)).save(entity);
    }

    @Test
    void getUserByIdNumber_shouldReturnUser() {
        when(repo.findById(1L)).thenReturn(Mono.just(entity)); // Repo retorna entidad
        when(mapper.toModel(entity)).thenReturn(user); // Mapper transforma a User

        StepVerifier.create(adapter.findUserById(1L))
                .expectNext(user)
                .verifyComplete();

        verify(repo, times(1)).findById(1L);
    }

    @Test
    void getUserByIdNumber_shouldThrowNotFoundException() {
        when(repo.findById(2L)).thenReturn(Mono.empty()); // Repo no encuentra entidad

        StepVerifier.create(adapter.findUserById(2L))
                .expectErrorMatches(throwable -> throwable instanceof com.crediya.authenticacion.usecase.exceptions.NotFoundException &&
                        throwable.getMessage().contains("User not found "))
                .verify();

        verify(repo, times(1)).findById(2L);
    }

    @Test
    void editUser_shouldUpdateExistingUser() {
        User updatedUser = new User(1L, "Ana", "Lopez", LocalDate.parse("1990-01-01"), "Calle 1", "3000000",
                "ana@test.com", 2000, "123", BigDecimal.valueOf(1));
        UserEntity updatedEntity = new UserEntity(1L, "Ana", "Lopez", LocalDate.parse("1990-01-01"), "Calle 1", "3000000",
                "ana@test.com", 2000, "123", 1L);

        when(repo.findById(1L)).thenReturn(Mono.just(entity)); // Repo encuentra usuario
        when(mapper.toEntity(updatedUser)).thenReturn(updatedEntity); // Mapper transforma
        when(repo.save(updatedEntity)).thenReturn(Mono.just(updatedEntity)); // Repo guarda cambios
        when(mapper.toModel(updatedEntity)).thenReturn(updatedUser);
        when(tx.transactional(any(Mono.class))).thenAnswer(i -> i.getArgument(0)); // Transacción simulada

        StepVerifier.create(adapter.editUser(updatedUser))
                .expectNext(updatedUser)
                .verifyComplete();

        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).save(updatedEntity);
    }

    @Test
    void deleteUser_shouldCallRepo() {
        when(repo.deleteById(1L)).thenReturn(Mono.empty()); // Repo elimina usuario
        when(tx.transactional(any(Mono.class))).thenAnswer(i -> i.getArgument(0)); // Simula transacción

        StepVerifier.create(adapter.deleteUser(1L))
                .verifyComplete();

        verify(repo, times(1)).deleteById(1L);
    }


}

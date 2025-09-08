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

    private static final Long USER_ID = 1L;
    private static final BigDecimal ROLE_ID = BigDecimal.ONE;

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
        user = buildUser(USER_ID, "Ana", "Martinez");
        entity = buildEntity(USER_ID, "Ana", "Martinez");
    }

    private User buildUser(Long id, String firstName, String lastName) {
        return User.builder()
                .id(id)
                .name(firstName)
                .lastName(lastName)
                .birthDate(LocalDate.parse("1990-01-01"))
                .address("Calle 1")
                .phone("3000000")
                .email("juan.perez@correo.com")
                .baseSalary(2000)
                .identificationNumber("123")
                .idRole(ROLE_ID)
                .password("hashedPassword")
                .build();
    }

    private UserEntity buildEntity(Long id, String firstName, String lastName) {
        return UserEntity.builder()
                .id(id)
                .name(firstName)
                .lastName(lastName)
                .birthDate(LocalDate.parse("1990-01-01"))
                .address("Calle 1")
                .phone("3000000")
                .email("juan.perez@correo.com")
                .baseSalary(2000)
                .identificationNumber("123")
                .roleId(ROLE_ID.longValue())
                .password("hashedPassword")
                .build();
    }

    private void mockTransactional(Mono<?> mono){
        when(tx.transactional(any(Mono.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
     void saveUser_shouldReturnSavedUser() {
        // Configuramos comportamiento de mocks
        mockTransactional(Mono.just(user));
        when(mapper.toEntity(user)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(user);

        StepVerifier.create(adapter.saveUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(repo).save(entity);
    }

    @Test
    void getUserByIdNumber_shouldReturnUser() {
        when(repo.findById(USER_ID)).thenReturn(Mono.just(entity));
        when(mapper.toModel(entity)).thenReturn(user);

        StepVerifier.create(adapter.findUserById(USER_ID))
                .expectNext(user)
                .verifyComplete();

        verify(repo).findById(USER_ID);
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
        User updatedUser = user.toBuilder().lastName("Lopez").build(); ;
        UserEntity updatedEntity = buildEntity(USER_ID, "Ana", "Lopez") ;

        when(repo.findById(USER_ID)).thenReturn(Mono.just(entity)); // Repo encuentra usuario
        when(mapper.toEntity(updatedUser)).thenReturn(updatedEntity); // Mapper transforma
        when(repo.save(updatedEntity)).thenReturn(Mono.just(updatedEntity)); // Repo guarda cambios
        when(mapper.toModel(updatedEntity)).thenReturn(updatedUser);
        mockTransactional(Mono.just(updatedUser));
        StepVerifier.create(adapter.editUser(updatedUser))
                .expectNext(updatedUser)
                .verifyComplete();

        verify(repo, times(1)).findById(1L);
        verify(repo, times(1)).save(updatedEntity);
    }

    @Test
    void deleteUser_shouldCallRepo() {
        when(repo.deleteById(USER_ID)).thenReturn(Mono.empty()); // Repo elimina usuario
        mockTransactional(Mono.empty());

        StepVerifier.create(adapter.deleteUser(USER_ID))
                .verifyComplete();

        verify(repo).deleteById(USER_ID);
    }


}

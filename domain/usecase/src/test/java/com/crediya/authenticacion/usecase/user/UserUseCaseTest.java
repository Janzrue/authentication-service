package com.crediya.authenticacion.usecase.user;

import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.model.user.gateways.UserRepository;
import com.crediya.authenticacion.usecase.exceptions.DuplicateException;
import com.crediya.authenticacion.usecase.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserUseCaseTest {
    @Mock
    private UserRepository userRepository; //  Mock: simulamos el acceso a la BD.

    @InjectMocks
    private UserUseCase userUseCase; //Clase que vamos a probar.

    private User user; //  Usuario de ejemplo para pruebas.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks.

        //  Instanciamos un usuario válido
        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setLastName("Perez");
        user.setEmail("juan@test.com");
        user.setIdentificationNumber("12345678");
        user.setBaseSalary(2000000);
        user.setBirthDate(LocalDate.parse("2000-11-10"));
        user.setIdRole(new BigDecimal("1"));
    }

    @Test
    void saveUser_ok() {
        //  Simulamos que NO existen duplicados
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existsByIdentificationNumber(user.getIdentificationNumber())).thenReturn(Mono.just(false));

        //  Simulamos que el repo guarda el usuario
        when(userRepository.saveUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectNext(user) //  Esperamos que retorne el usuario
                .verifyComplete();

        //  Validamos que efectivamente se llamó al saveUser del repo
        verify(userRepository).saveUser(user);
    }

    @Test
    void saveUser_duplicateEmail() {
        //  Simulamos que el correo ya existe
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
        when(userRepository.existsByIdentificationNumber(user.getIdentificationNumber())).thenReturn(Mono.just(false));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(DuplicateException.class) //  Debe lanzar excepción
                .verify();
    }

    @Test
    void findUserById_exist() {
        //  Simulamos que el usuario existe
        when(userRepository.findUserById(1L)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.findUserByIdNumber(1L))
                .expectNext(user) //  Retorna usuario
                .verifyComplete();
    }

    @Test
    void findUserById_notExist() {
        //  Simulamos que el usuario NO existe
        when(userRepository.findUserById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.findUserByIdNumber(1L))
                .expectError(NotFoundException.class) //  Debe lanzar excepción
                .verify();
    }

    @Test
    void editUser_ok() {
        //  Simulamos que el usuario existe y luego lo actualiza
        when(userRepository.findUserById(1L)).thenReturn(Mono.just(user));
        when(userRepository.editUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.editUser(user))
                .expectNext(user) //  Usuario actualizado
                .verifyComplete();
    }

    @Test
    void deleteUser_ok() {
        //  Simulamos que el usuario existe y se elimina
        when(userRepository.findUserById(1L)).thenReturn(Mono.just(user));
        when(userRepository.deleteUser(1L)).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.deleteUser(1L))
                .verifyComplete(); //  Terminó bien
    }
}

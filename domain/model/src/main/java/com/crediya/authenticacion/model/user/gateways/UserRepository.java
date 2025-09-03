package com.crediya.authenticacion.model.user.gateways;

import com.crediya.authenticacion.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Puerto (interfaz) del repositorio de usuarios.
 *
 * - Define las operaciones que el sistema necesita para manipular usuarios.
 * - Hace parte del dominio y sigue el principio de inversión de dependencias (Clean Architecture).
 */

public interface UserRepository {


    Mono<User> saveUser(User user);
    Flux<User> findAllUsers();
    Mono<User> findUserById(Long id);
    Mono<User> editUser(User user);
    Mono<Void> deleteUser(Long id);


    /**
     * Validar si ya existe un usuario con el correo indicado.
     * @param email correo electrónico.
     * @return Mono<Boolean> true si ya existe.
     */
    Mono<Boolean> existsByEmail(String email);

    /**
     * Validar si ya existe un usuario con el documento de identidad indicado.
     * @param identificationNumber número de documento.
     * @return Mono<Boolean> true si ya existe.
     */
    Mono<Boolean> existsByIdentificationNumber(String identificationNumber);

    /**
     * Verificar si existe un rol asociado al usuario mediante su id.
     * @param idRole identificador del rol.
     * @return Mono<Boolean> true si el rol existe.
     */
    Mono<Boolean> existsRoleById(Long idRole);
}

package com.crediya.authenticacion.model.user.gateways;

import com.crediya.authenticacion.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface UserRepository {

    Mono<User> saveUser(User user);
    Flux<User> findAllUsers();
    Mono<User> findUserById(Long id);
    Mono<User> editUser(User user);
    Mono<Void> deleteUser(Long id);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdentificationNumber(String identificationNumber);
    Mono<Boolean> existsRoleById(Long idRole);
}

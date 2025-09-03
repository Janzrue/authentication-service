package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para la entidad UserEntity.
 *
 * Explicación:
 * - Extiende ReactiveCrudRepository para operaciones CRUD básicas (findAll, findById, save, delete).
 * - Extiende ReactiveQueryByExampleExecutor para búsquedas dinámicas usando Query by Example.
 * - Define métodos específicos para verificar existencia de usuarios por correo, documento o rol.
 */

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdentificationNumber(String IdentificationNumber);
    Mono<Boolean> existsRoleById(Long roleId);
    Mono<UserEntity> findByEmail(String email);
}

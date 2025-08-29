package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdentificationNumber(String IdentificationNumber);
    Mono<Boolean> existsRoleById(Long roleId);
}

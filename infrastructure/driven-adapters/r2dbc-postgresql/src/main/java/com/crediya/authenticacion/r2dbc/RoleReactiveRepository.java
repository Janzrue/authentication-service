package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.r2dbc.entity.RoleEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repositorio reactivo para "roles".
 * Extiende ReactiveCrudRepository para operaciones CRUD reactivas.
 */
public interface RoleReactiveRepository extends
        ReactiveCrudRepository <RoleEntity, Integer>,
        ReactiveQueryByExampleExecutor <RoleEntity> {
}

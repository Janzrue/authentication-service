package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.model.user.gateways.UserRepository;
import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import com.crediya.authenticacion.r2dbc.helper.ReactiveAdapterOperations;
import com.crediya.authenticacion.r2dbc.mapper.UserR2dbcMapper;
import com.crediya.authenticacion.usecase.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class UserReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<User, UserEntity, Long, UserReactiveRepository>
        implements UserRepository {

    private final UserReactiveRepository userReactiveRepository;
    private final UserR2dbcMapper userMapper;
    private final TransactionalOperator transactionalOperator;

    /**
     * Adaptador que implementa UserRepository del dominio.
     *
     * Explicación:
     * - Conecta la capa de dominio (User) con la base de datos (UserEntity).
     * - Usa Spring Data R2DBC para consultas reactivas.
     * - Incluye manejo transaccional con TransactionalOperator para garantizar atomicidad.
     */

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, UserR2dbcMapper userMapper, TransactionalOperator transactionalOperator) {
        super(repository, null, userMapper::toModel);
        this.userReactiveRepository = repository;
        this.userMapper = userMapper;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<User> saveUser(User user) {
        return Mono.defer(() -> {
            // Convierte el modelo de dominio a entidad persistente
            UserEntity entity = userMapper.toEntity(user);
            // Guarda la entidad y la convierte nuevamente a modelo
            return userReactiveRepository.save(entity)
                    .map(userMapper::toModel);
        }).as(transactionalOperator::transactional); // Aplica transacción reactiva
    }

    @Override
    public Flux<User> findAllUsers() {
        return super.repository.findAll()
                .map(userMapper::toModel);
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return userReactiveRepository.findById(id)
                .map(userMapper::toModel)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + id)));
    }

    @Override
    public Mono<User> editUser(User user) {
        return userReactiveRepository.findById(user.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id:")))
                .flatMap(existing -> {
                    // Convierte el modelo actualizado a entidad
                    UserEntity updated = userMapper.toEntity(user);
                    // Mantiene el ID original de la base
                    updated.setId(existing.getId());
                    // Guarda cambios y devuelve el modelo
                    return userReactiveRepository.save(updated)
                            .map(userMapper::toModel);
                })
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userReactiveRepository.deleteById(id)
                .as(transactionalOperator::transactional);
    }

    // Verifica si ya existe un usuario con un correo electrónico dado.
    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userReactiveRepository.existsByEmail(email);
    }

    // Verifica si ya existe un usuario con un número de identificación dado.
    @Override
    public Mono<Boolean> existsByIdentificationNumber(String identificationNumber) {
        return userReactiveRepository.existsByIdentificationNumber(identificationNumber);
    }

    // Verifica si ya existe un rol con un ID dado.
    @Override
    public Mono<Boolean> existsRoleById(Long idRole) {
        return userReactiveRepository.existsRoleById(idRole);
    }
}

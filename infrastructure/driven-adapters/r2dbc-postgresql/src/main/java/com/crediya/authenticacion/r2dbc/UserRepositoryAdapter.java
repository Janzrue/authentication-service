package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.model.user.gateways.UserRepository;
import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import com.crediya.authenticacion.r2dbc.helper.ReactiveAdapterOperations;
import com.crediya.authenticacion.r2dbc.mapper.UserR2dbcMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class UserRepositoryAdapter
        extends ReactiveAdapterOperations<User, UserEntity, Long, UserReactiveRepository>
        implements UserRepository {

    private final UserR2dbcMapper userMapper;

    public UserRepositoryAdapter(UserReactiveRepository repository, UserR2dbcMapper userMapper) {
        super(repository, null, userMapper::toModel);
        this.userMapper = userMapper;
    }

    @Override
    public Mono<User> saveUser(User user) {
        UserEntity entity = userMapper.toEntity(user);
        return super.repository.save(entity)
                .map(userMapper::toModel);
    }

    @Override
    public Flux<User> findAllUsers() {
        return super.repository.findAll()
                .map(userMapper::toModel);
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return super.repository.findById(id)
                .map(userMapper::toModel);
    }

    @Override
    public Mono<User> editUser(User user) {
        return super.repository.findById(user.getIdNumber())
                .flatMap(existing -> {
                    UserEntity updated = userMapper.toEntity(user);
                    return super.repository.save(updated);
                })
                .map(userMapper::toModel);
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return super.repository.deleteById(id);
    }
}

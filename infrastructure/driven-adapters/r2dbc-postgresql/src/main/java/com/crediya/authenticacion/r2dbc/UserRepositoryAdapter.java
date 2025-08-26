package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.User;
import com.crediya.authenticacion.model.gateways.UserRepository;
import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import com.crediya.authenticacion.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
        > implements UserRepository {
    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

}

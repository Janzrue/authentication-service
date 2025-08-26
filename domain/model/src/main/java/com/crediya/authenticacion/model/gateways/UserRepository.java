package com.crediya.authenticacion.model.gateways;

import com.crediya.authenticacion.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(User user);

    Flux<User> findAll();
}

package com.crediya.authenticacion.usecase.user;


import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.saveUser(user);
    }
}

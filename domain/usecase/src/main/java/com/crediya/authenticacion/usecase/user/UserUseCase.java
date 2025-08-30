package com.crediya.authenticacion.usecase.user;


import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.model.user.gateways.UserRepository;
import com.crediya.authenticacion.usecase.exceptions.DuplicateException;
import com.crediya.authenticacion.usecase.exceptions.NotFoundException;
import com.crediya.authenticacion.usecase.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        validateUser(user);

        return Mono.zip(
                userRepository.existsByEmail(user.getEmail()),
                userRepository.existsByIdentificationNumber(user.getIdentificationNumber())
        ).flatMap(tuple -> {
            boolean emailExists = tuple.getT1();
            boolean docExists = tuple.getT2();

            if (emailExists) return Mono.error(new DuplicateException("The email is already registered."));
            if (docExists) return Mono.error(new DuplicateException("The document has already been registered."));

            return userRepository.saveUser(user);
        });
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<User> findUserByIdNumber(Long idNumber) {
        return userRepository.findUserById(idNumber)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + idNumber)));
    }

    public Mono<User> editUser(User user) {
        return userRepository.findUserById(user.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + user.getId())))
                .flatMap(existing -> {
                    validateUser(user); // valida datos antes de actualizar
                    return userRepository.editUser(user);
                });
    }

    public Mono<Void> deleteUser(Long idNumber) {
        return userRepository.findUserById(idNumber)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + idNumber)))
                .flatMap(existing -> userRepository.deleteUser(idNumber));
    }


    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<Boolean> existsByIdentificationNumber(String IdentificationNumber) {
        return userRepository.existsByIdentificationNumber(IdentificationNumber);
    }

    private void validateUser(User user) {
        if (isNullOrEmpty(user.getName())) throw new ValidationException("The name cannot be empty.");
        if (isNullOrEmpty(user.getLastName())) throw new ValidationException("The last name cannot be empty.");

        validateEmail(user.getEmail());
        validatebaseSalary(user.getBaseSalary());
        validateIdentificationNumber(user.getIdentificationNumber());
        validateIdRole(user.getIdRole());
    }

    private void validateEmail(String email) {
        if (isNullOrEmpty(email)) throw new ValidationException("Email is required.");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) throw new ValidationException("Invalid email format");
    }

    private void validatebaseSalary(Integer baseSalary) {
        if (baseSalary == null) throw new ValidationException("The base salary is mandatory.");
        if (baseSalary < 0 || baseSalary > 15000000)
            throw new ValidationException("The base salary must be between 0 and 15,000,000.");
    }

    private void validateIdentificationNumber(String identificationNumber) {
        if (isNullOrEmpty(identificationNumber)) throw new ValidationException("Identification number is required.");
    }

    private void validateIdRole(BigDecimal idRole) {
        if (idRole == null) throw new ValidationException("The Id Role is mandatory.");
        if (idRole.compareTo(BigDecimal.ZERO) < 0)
            throw new ValidationException("The Id Role must be greater than 0.");
    }



    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}

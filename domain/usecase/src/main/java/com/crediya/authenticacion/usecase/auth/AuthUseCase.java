package com.crediya.authenticacion.usecase.auth;

import com.crediya.authenticacion.model.auth.AuthConstants;
import com.crediya.authenticacion.model.auth.AuthCredentials;
import com.crediya.authenticacion.model.auth.gateways.PasswordEncoderPort;
import com.crediya.authenticacion.model.tokeninfo.TokenInfo;
import com.crediya.authenticacion.model.tokeninfo.gateways.TokenProvider;
import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.model.user.gateways.UserRepository;
import com.crediya.authenticacion.usecase.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoderPort passwordEncoderPort;
    private final RolesResolver rolesResolver;

    public Mono<TokenInfo> login(AuthCredentials credentials) {
        System.out.println(credentials);
        if (credentials == null || isBlank(credentials.getEmail()) || isBlank(credentials.getPassword())) {
            return Mono.error(new ValidationException("email y password son obligatorios"));
        }

        return userRepository.findByEmail(credentials.getEmail())
                .switchIfEmpty(Mono.error(new ValidationException(AuthConstants.MSG_INVALID_CREDENTIALS)))

                .flatMap(user -> passwordEncoderPort.matches(credentials.getPassword(), user.getPassword())
                        .flatMap(matches -> {

                            if (!matches)
                                return Mono.error(new ValidationException(AuthConstants.MSG_INVALID_CREDENTIALS));
                            List<String> roles = rolesResolver.resolve(user);
                            return Mono.zip(
                                    tokenProvider.generateAccessToken(user, roles, AuthConstants.ACCESS_TOKEN_TTL_MS),
                                    tokenProvider.generateRefreshToken(user, AuthConstants.REFRESH_TOKEN_TTL_MS)
                            ).map(t -> TokenInfo.builder()
                                    .tokenType("Bearer")
                                    .accessToken(t.getT1())
                                    .refreshToken(t.getT2())
                                    .expiresIn(AuthConstants.ACCESS_TOKEN_TTL_MS / 1000)
                                    .build());
                        })
                );
    }

    public Mono<TokenInfo> refresh(String refreshToken) {
        if (isBlank(refreshToken))
            return Mono.error(new ValidationException(AuthConstants.MSG_INVALID_TOKEN));

        return tokenProvider.parseAndValidate(refreshToken)
                .flatMap(claims -> {
                    Object email = claims.get(AuthConstants.CLAIM_EMAIL);
                    if (email == null)
                        return Mono.error(new ValidationException(AuthConstants.MSG_INVALID_TOKEN));
                    return userRepository.findByEmail(email.toString());
                })
                .flatMap(user -> {
                    List<String> roles = rolesResolver.resolve(user);
                    return Mono.zip(
                            tokenProvider.generateAccessToken(user, roles, AuthConstants.ACCESS_TOKEN_TTL_MS),
                            tokenProvider.generateRefreshToken(user, AuthConstants.REFRESH_TOKEN_TTL_MS)
                    ).map(t -> TokenInfo.builder()
                            .tokenType("Bearer")
                            .accessToken(t.getT1())
                            .refreshToken(t.getT2())
                            .expiresIn(AuthConstants.ACCESS_TOKEN_TTL_MS / 1000)
                            .build());
                });
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public interface RolesResolver {
        List<String> resolve(User user);
    }
}

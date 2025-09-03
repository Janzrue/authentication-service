package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.auth.AuthConstants;
import com.crediya.authenticacion.model.tokeninfo.gateways.TokenProvider;
import com.crediya.authenticacion.model.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtTokenProviderAdapter implements TokenProvider {

    private final SecretKey secretKey;
    private final JwtParser parser;

    public JwtTokenProviderAdapter(@Value("${security.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.parser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
    }

    @Override
    public Mono<String> generateAccessToken(User user, List<String> roles, long ttlMs) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttlMs);
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim(AuthConstants.CLAIM_ID, user.getId())
                .claim(AuthConstants.CLAIM_EMAIL, user.getEmail())
                .claim(AuthConstants.CLAIM_NAME, user.getName() + " " + user.getLastName())
                .claim(AuthConstants.CLAIM_ROLES, roles)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return Mono.just(token);
    }

    @Override
    public Mono<String> generateRefreshToken(User user, long ttlMs) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttlMs);
        String token = Jwts.builder()
                .setSubject("refresh:" + user.getEmail())
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim(AuthConstants.CLAIM_EMAIL, user.getEmail())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return Mono.just(token);
    }

    @Override
    public Mono<Map<String, Object>> parseAndValidate(String token) {
        return Mono.fromCallable(() -> {
            Jws<Claims> jws = parser.parseClaimsJws(token);
            return new HashMap<>(jws.getBody());
        });
    }
}

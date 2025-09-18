package com.crediya.authenticacion.model.auth;
public final class AuthConstants {
    private AuthConstants(){}

    // Headers / Bearer
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Claims
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_ID = "uid";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_NAME = "name";

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ASESOR = "ASESOR";
    public static final String ROLE_CLIENTE = "CLIENTE";

    // Tiempos (ms)
    public static final long ACCESS_TOKEN_TTL_MS  = 30 * 60_000L;        // 5 minutos
    public static final long REFRESH_TOKEN_TTL_MS = 60 * 60_000L;   // 15 minutos

    // Endpoints auth
    public static final String LOGIN_PATH   = "/api/v1/login";
    public static final String REFRESH_PATH = "/api/v1/token/refresh";

    // Mensajes
    public static final String MSG_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String MSG_INVALID_TOKEN       = "Invalid token";
    public static final String MSG_FORBIDDEN_ACTION    = "You don't have permission for this action.";
}

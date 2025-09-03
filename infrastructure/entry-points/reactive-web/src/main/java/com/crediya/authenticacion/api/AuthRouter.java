package com.crediya.authenticacion.api;

import com.crediya.authenticacion.model.auth.AuthConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler authHandler) {
        return route(POST(AuthConstants.LOGIN_PATH), authHandler::login)
                .andRoute(POST(AuthConstants.REFRESH_PATH), authHandler::refresh);
    }
}

package com.crediya.authenticacion.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoleRouterRest {


    @Bean
    public RouterFunction<ServerResponse> rolRoutes(RoleHandler rolHandler) {
        return route(POST("/api/v1/roles"), rolHandler::listenSaveRole)
                .andRoute(GET("/api/v1/roles/{uniqueId}"), rolHandler::listenFindRoleById);
    }
}

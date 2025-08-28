package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.config.UserPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST("/api/v1/users"), handler::listenSaveUser);
        // .and(route(GET("/api/v1/users"), handler::UserGetAll));
//                                        .and(route(GET("/api/v1/users/exists/{email}"), handler::UserExistsByEmail))
//                                        .and(route(GET("/api/v1/users/exists/{id}"), handler::UserExistsById))
//                                        .and(route(POST("/api/v1/users/login"), handler::UserLogin))
//                                        .and(route(POST("/api/v1/users/logout"), handler::UserLogout))
//                                        .and(route(PATCH("/api/v1/users/password/{id}"), handler::UserUpdatePassword))
//                                .and(route(GET("/api/v1/users/{id}"), handler::UserGetById)

    }
}

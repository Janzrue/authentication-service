package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Router funcional para exponer endpoints relacionados con USUARIOS.
 * Usa programación funcional de Spring WebFlux.
 */

@Configuration
@Tag(name = "Users API", description = "CRUD Operations for Users")
public class UserRouterRest {

    private static final String BASE_PATH = "/api/v1/users";
    private static final String PATH_ID = "/{id}";
    private static final String PATH_EXISTS_EMAIL = "/exists/email/{email}";
    private static final String PATH_EXISTS_DOC = "/exists/idNumber/{idNumber}";

    @Bean
    @RouterOperations({

            // ================== GET ALL ==================
            @RouterOperation(
                    path = BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenFindAllUsers",
                    operation = @Operation(
                            operationId = "listenFindAllUsers",
                            summary = "Get all users",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User list",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    )
                            }
                    )
            ),

            // ================== GET BY ID ==================
            @RouterOperation(
                    path = BASE_PATH + PATH_ID,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenFindUserByIdNumber",
                    operation = @Operation(
                            operationId = "listenFindUserByIdNumber",
                            summary = "Get a user by ID",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User found",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            }
                    )
            ),

            // ================== POST (REGISTRAR) ==================
            @RouterOperation(
                    path = BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "listenSaveUser",
                            summary = "Register a new user",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = UserDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User created",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    )
                            }
                    )
            ),

            // ================== PUT (EDITAR) ==================
            @RouterOperation(
                    path = BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = UserHandler.class,
                    beanMethod = "listenEditUser",
                    operation = @Operation(
                            operationId = "listenEditUser",
                            summary = "Modify an existing user",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Updated user",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            }
                    )
            ),

            // ================== DELETE ==================
            @RouterOperation(
                    path = BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.DELETE,
                    beanClass = UserHandler.class,
                    beanMethod = "listenDeleteUser",
                    operation = @Operation(
                            operationId = "listenDeleteUser",
                            summary = "Delete a user by ID",
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "User removed"),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST(BASE_PATH), handler::listenSaveUser)
                .andRoute(GET(BASE_PATH), handler::listenFindAllUsers)
                .andRoute(GET(BASE_PATH + PATH_ID), handler::listenFindUserById)
                .andRoute(PUT(BASE_PATH), handler::listenEditUser)
                .andRoute(DELETE(BASE_PATH + PATH_ID), handler::listenDeleteUser)
                .andRoute(GET(BASE_PATH + PATH_EXISTS_EMAIL), handler::existsByEmail)
                .andRoute(GET(BASE_PATH + PATH_EXISTS_DOC), handler::existsByIdentificationNumber);
    }
}

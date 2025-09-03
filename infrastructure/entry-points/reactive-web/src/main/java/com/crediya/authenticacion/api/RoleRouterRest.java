package com.crediya.authenticacion.api;

import com.crediya.authenticacion.api.dto.RoleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Router para exponer endpoints de ROLES.
 */

@Configuration
public class RoleRouterRest {

    // Constantes
    private static final String BASE_PATH = "/api/v1/roles";
    private static final String PATH_ID = "/{id}";

    @Bean
    @RouterOperations({

            @RouterOperation(
                    path = BASE_PATH + PATH_ID,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = RoleHandler.class,
                    beanMethod = "listenFindRoleById",
                    operation = @Operation(
                            operationId = "listenFindRoleById",
                            summary = "Get a role by ID",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Role found",
                                            content = @Content(schema = @Schema(implementation = RoleDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Role not found")
                            }
                    )
            ),

            @RouterOperation(
                    path = BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = RoleHandler.class,
                    beanMethod = "listenSaveRole",
                    operation = @Operation(
                            operationId = "listenSaveRole",
                            summary = "Save a new role",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = RoleDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Role saved"),
                            }
                    )
            ),

            @RouterOperation(
                    path = BASE_PATH,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = RoleHandler.class,
                    beanMethod = "listenUpdateRole",
                    operation = @Operation(
                            operationId = "listenUpdateRole",
                            summary = "Edit an existing role",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Role updated"),
                                    @ApiResponse(responseCode = "404", description = "Role not updated")
                            }
                    )
            ),

            @RouterOperation(
                    path = BASE_PATH + PATH_ID,
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.DELETE,
                    beanClass = RoleHandler.class,
                    beanMethod = "listenDeleteRole", // 🔄 cambiado
                    operation = @Operation(
                            operationId = "listenDeleteRole",
                            summary = "Delete an role by ID",
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Role removed"),
                                    @ApiResponse(responseCode = "404", description = "Role not found")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> roleRoutes(RoleHandler rolHandler) {
        return route(POST(BASE_PATH), rolHandler::listenSaveRole)
                .andRoute(GET(BASE_PATH + PATH_ID), rolHandler::listenFindRoleById)
                .andRoute(PUT(BASE_PATH), rolHandler::listenUpdateRole)
                .andRoute(DELETE(BASE_PATH + PATH_ID), rolHandler::listenDeleteRole);
    }
}

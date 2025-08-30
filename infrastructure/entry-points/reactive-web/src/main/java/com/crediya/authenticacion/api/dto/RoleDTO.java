package com.crediya.authenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
@Schema(description = "Object representing a role within the system")
public class RoleDTO {

    @Schema(description = "Unique identifier for the role", example = "1")
    private Long id;

    @NotBlank(message = "The name cannot be empty.")
    @Size(min = 3, max = 50, message = "The name must be between 3 and 50 characters long.")
    @Schema(description = "Name of the role", example = "ADMIN")
    private String name;

    @Size(max = 200, message = "The description should not exceed 200 characters.")
    @Schema(description = "Description of the role", example = "Administrator with full access")
    private String description;
}

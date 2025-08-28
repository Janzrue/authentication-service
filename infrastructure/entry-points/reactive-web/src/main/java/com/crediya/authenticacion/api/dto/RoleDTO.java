package com.crediya.authenticacion.api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
public class RoleDTO {

    private Integer uniqueId;

    @NotBlank(message = "The name cannot be empty.")
    @Size(min = 3, max = 50, message = "The name must be between 3 and 50 characters long.")
    private String name;

    @Size(max = 200, message = "The description should not exceed 200 characters.")
    private String description;
}

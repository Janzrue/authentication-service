package com.crediya.authenticacion.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto que representa la petición para crear o actualizar un usuario")
public class UserDTO {

        @NotBlank(message = "Name is mandatory.")
        @Schema(description = "User name", example = "Juan")
        private String name;

        @NotBlank(message = "Last name is mandatory.")
        @Schema(description = "User last name", example = "Perez")
        private String lastName;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "birthDate must be yyyy-MM-dd")
        @Schema(description = "User birth date (yyyy-mm-dd)", example = "1990-01-01")
        private LocalDate birthDate;
        @Schema(description = "User address", example = "123 Main St")
        private String address;
        @Schema(description = "User phone number", example = "1234567890")
        private String phone;

        @NotBlank(message = "Email is mandatory.")
        @Email(message = "Email format is invalid.")
        private String email;

        @NotNull(message = "Base salary is mandatory.")
        @Min(value = 0, message = "Salary cannot be negative.")
        @Max(value = 15000000, message = "Salary cannot be greater than 15,000,000.")
        @Schema(description = "User base salary", example = "5000000")
        private Integer baseSalary;

        @NotBlank(message = "identification number is mandatory.")
        @Schema(description = "User identification number", example = "123456789")
        private String identificationNumber;

        @NotNull(message = "roleId is mandatory.")
        @Min(value = 1, message = "roleId must be greater than 0.")
        @Schema(description = "Role ID assigned to the user", example = "1")
        private Integer roleId;

}

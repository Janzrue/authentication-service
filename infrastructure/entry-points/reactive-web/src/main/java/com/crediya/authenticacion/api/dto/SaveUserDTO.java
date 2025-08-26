package com.crediya.authenticacion.api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record SaveUserDTO(
        @NotBlank(message = "Name is mandatory.")
        String name,

        @NotBlank(message = "Last name is mandatory.")
        String lastName,

        LocalDate birthDate,
        String address,
        String phone,

        @NotBlank(message = "Email is mandatory.")
        @Email(message = "Email format is invalid.")
        String email,

        @NotNull(message = "Base salary is mandatory.")
        @Min(value = 0, message = "Salary cannot be negative.")
        @Max(value = 15000000, message = "Salary cannot be greater than 15,000,000.")
        @Digits(integer = 8, fraction = 0, message = "Base salary must be a valid number.")
        Integer baseSalary,

        @NotBlank(message = "identification number is mandatory.")
        String identificationNumber
) {
}

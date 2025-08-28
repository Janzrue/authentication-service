package com.crediya.authenticacion.api.dto;


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
public class SaveUserDTO {
        @NotBlank(message = "Name is mandatory.")
        private String name;

        @NotBlank(message = "Last name is mandatory.")
        String lastName;

        LocalDate birthDate;
        String address;
        String phone;

        @NotBlank(message = "Email is mandatory.")
        @Email(message = "Email format is invalid.")
        String email;

        @NotNull(message = "Base salary is mandatory.")
        @Min(value = 0, message = "Salary cannot be negative.")
        @Max(value = 15000000, message = "Salary cannot be greater than 15,000,000.")
        @Digits(integer = 8, fraction = 0, message = "Base salary must be a valid number.")
        Integer baseSalary;

        @NotBlank(message = "identification number is mandatory.")
        String identificationNumber;

        @NotNull(message = "idRole is mandatory.")
        @Min(value = 1, message = "idRole must be greater than 0.")
        private Integer roleId;

}

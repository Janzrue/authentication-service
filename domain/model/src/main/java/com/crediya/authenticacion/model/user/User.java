package com.crediya.authenticacion.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

/**
 * Entidad de dominio que representa a un Usuario del sistema.
 *
 * - Contiene los atributos principales de un usuario.
 * - Hace parte del dominio limpio (sin dependencias de infraestructura).
 */

public class User {
    private Long id;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private Integer baseSalary;
    private String identificationNumber;

    /** Rol asociado al usuario (FK a Role). */
    private BigDecimal idRole;
}

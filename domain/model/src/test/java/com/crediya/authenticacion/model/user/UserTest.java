package com.crediya.authenticacion.model.user;

import com.crediya.authenticacion.model.role.Role;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias para la entidad User.
 *
 * - Aseguran que el builder, setters y constructores funcionan como se espera.
 */

public class UserTest {
    @Test
    void shouldCreateUserUsingBuilder() {

        User user = User.builder()
                .id(12345L)
                .name("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.parse("2000-11-10"))
                .address("Calle 123")
                .phone("3001234567")
                .email("juan.perez@test.com")
                .baseSalary(2500000)
                .identificationNumber("987654321")
                .idRole(BigDecimal.valueOf(1))
                .build();

        // Creamos un usuario usando el builder y validamos los valores
        assertEquals(12345L, user.getId());
        assertEquals("Juan", user.getName());
        assertEquals("Pérez", user.getLastName());
        assertEquals(LocalDate.parse("2000-11-10"), user.getBirthDate());
        assertEquals("Calle 123", user.getAddress());
        assertEquals("3001234567", user.getPhone());
        assertEquals("juan.perez@test.com", user.getEmail());
        assertEquals(2500000, user.getBaseSalary());
        assertEquals("987654321", user.getIdentificationNumber());
        assertEquals(BigDecimal.valueOf(1), user.getIdRole());
    }

    @Test
    void shouldModifyUserUsingSetters() {
        User user = new User();
        user.setId(123L);
        user.setName("Carlos");
        user.setLastName("Lopez");

        assertEquals(123L, user.getId());
        assertEquals("Carlos", user.getName());
        assertEquals("Lopez", user.getLastName());
    }

    @Test
    void shouldSupportAllArgsConstructor() {
        User user = new User(456L, "Ana", "Martínez", LocalDate.parse("1985-05-05"),
                "Carrera 45", "3100000000", "ana@test.com",
                3500000, "11223344",
                BigDecimal.valueOf(2));

        assertEquals(456L, user.getId());
        assertEquals("Ana", user.getName());
    }
}

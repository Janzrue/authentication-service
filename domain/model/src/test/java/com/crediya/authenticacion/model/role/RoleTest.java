package com.crediya.authenticacion.model.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias para la entidad Rol.
 *
 * - Validan que los constructores, builder y setters funcionan correctamente.
 */

public class RoleTest {
    @Test
    void shouldCreateRoleUsingBuilder() {

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrator role with full permissions")
                .build();


        // Validamos que los valores se asignaron correctamente
        assertEquals(1, role.getId());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role with full permissions", role.getDescription());
    }

    @Test
    void shouldModifyRoleUsingSetters() {

        Role role = new Role();

        role.setId(2L);
        role.setName("USER");
        role.setDescription("Standard user");

        // Validamos que se asignaron correctamente
        assertEquals(2, role.getId());
        assertEquals("USER", role.getName());
        assertEquals("Standard user", role.getDescription());
    }

    @Test
    void shouldSupportAllArgsConstructor() {

        Role role = new Role(3L, "MANAGER", "Manager role with elevated permissions");

        // Validamos los valores
        assertEquals(3, role.getId());
        assertEquals("MANAGER", role.getName());
        assertEquals("Manager role with elevated permissions", role.getDescription());
    }
}

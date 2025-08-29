package com.crediya.authenticacion.model.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RoleTest {
    @Test
    void shouldCreateRolUsingBuilder() {

        Role role = Role.builder()
                .uniqueId(1)
                .name("ADMIN")
                .description("Administrator role with full permissions")
                .build();


        assertEquals(1, role.getUniqueId());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role with full permissions", role.getDescription());
    }

    @Test
    void shouldModifyRolUsingSetters() {

        Role role = new Role();

        role.setUniqueId(2);
        role.setName("USER");
        role.setDescription("Standard user");

        assertEquals(2, role.getUniqueId());
        assertEquals("USER", role.getName());
        assertEquals("Standard user", role.getDescription());
    }

    @Test
    void sshouldSupportAllArgsConstructor() {

        Role role = new Role(3, "MANAGER", "Manager role with elevated permissions");

        assertEquals(3, role.getUniqueId());
        assertEquals("MANAGER", role.getName());
        assertEquals("Manager role with elevated permissions", role.getDescription());
    }
}

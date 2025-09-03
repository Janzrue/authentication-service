package com.crediya.authenticacion.model.role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad de dominio que representa un Rol dentro del sistema.
 *
 * - Pertenece al modelo del dominio (no depende de infraestructura).
 * - Representa perfiles como ADMIN, USER, GUEST, etc.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // Patrón de diseño que permite construir objetos de manera flexible.
public class Role {

    private Long id;
    private String name;
    private String description;
}

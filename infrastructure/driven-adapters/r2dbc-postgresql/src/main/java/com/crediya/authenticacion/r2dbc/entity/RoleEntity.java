package com.crediya.authenticacion.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Mapper de MapStruct para convertir entre:
 * - Entidad persistente (RoleEntity)
 * - Modelo de dominio (Rol)
 */

@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}
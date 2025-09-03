package com.crediya.authenticacion.model.role.gateways;

import com.crediya.authenticacion.model.role.Role;
import reactor.core.publisher.Mono;

/**
 * Puerto (interfaz) que define las operaciones del repositorio de roles.
 *
 * - Forma parte de la arquitectura hexagonal en la capa de dominio.
 * - Define qué operaciones deben implementarse, pero no cómo (eso lo hará la capa de infraestructura).
 * - Usa programación reactiva (Project Reactor), por eso devuelve Mono<Role>.
 */

public interface RoleRepository {

    Mono<Role> findRoleById(Long id);
    Mono<Role> saveRole(Role role);
    Mono<Role> updateRole(Role role);
    Mono<Void> deleteRole(Long id);
}

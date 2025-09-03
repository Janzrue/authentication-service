package com.crediya.authenticacion.usecase.role;

import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Caso de uso (lógica de negocio) para manejar ROLE.
 *
 * - Usa programación reactiva (Mono) porque se asume que el acceso
 *   al repositorio es NO bloqueante (reactivo).
 */

@RequiredArgsConstructor
public class RoleUseCase {

    // Inyección de la dependencia del repositorio de roles.
    private final RoleRepository roleRepository;

    // Métodos que implementan la lógica de negocio para roles.

    public Mono<Role> findRoleById(Long id){
        return roleRepository.findRoleById(id);
    }

    public Mono<Role> saveRole(Role role){
        return roleRepository.saveRole(role);
    }

    public Mono<Role> updateRole(Role role){
        return roleRepository.updateRole(role);
    }

    public Mono<Void> deleteRole(Long id){
        return roleRepository.deleteRole(id);
    }
}

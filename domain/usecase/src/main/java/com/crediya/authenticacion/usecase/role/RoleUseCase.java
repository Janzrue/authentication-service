package com.crediya.authenticacion.usecase.role;

import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleRepository roleRepository;

    public Mono<Role> findRoleById(Long id){
        return roleRepository.findRoleById(id);
    }

    public Mono<Role> saveRole(Role role){
        return roleRepository.saveRole(role);
    }
}

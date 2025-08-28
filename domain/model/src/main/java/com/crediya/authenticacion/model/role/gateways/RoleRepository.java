package com.crediya.authenticacion.model.role.gateways;

import com.crediya.authenticacion.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {

    Mono<Role> findRoleById(Long id);
    Mono<Role> saveRole(Role role);
}

package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.model.role.gateways.RoleRepository;
import com.crediya.authenticacion.r2dbc.entity.RoleEntity;
import com.crediya.authenticacion.r2dbc.helper.ReactiveAdapterOperations;
import com.crediya.authenticacion.r2dbc.mapper.RoleMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class RoleReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Role, RoleEntity, Integer, RoleReactiveRepository>
        implements RoleRepository {

    private final RoleMapper roleMapper;
    private final RoleReactiveRepository roleReactiveRepository;

    protected RoleReactiveRepositoryAdapter(RoleReactiveRepository roleReactiveRepository, RoleMapper roleMapper){
        super(roleReactiveRepository, null, roleMapper::toModel);
        this.roleReactiveRepository = roleReactiveRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Mono<Role> findRoleById(Long id) {
        return roleReactiveRepository.findById(id.intValue())
                .map(roleMapper::toModel);
    }

    @Override
    public Mono<Role> saveRole(Role role) {
        return roleReactiveRepository.save(roleMapper.toEntity(role))
                .map(roleMapper::toModel);
    }

}

package com.crediya.authenticacion.r2dbc;

import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.model.role.gateways.RoleRepository;
import com.crediya.authenticacion.r2dbc.entity.RoleEntity;
import com.crediya.authenticacion.r2dbc.helper.ReactiveAdapterOperations;
import com.crediya.authenticacion.r2dbc.mapper.RoleMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Adaptador que implementa RolRepository del dominio.
 *
 * Explicación:
 * - Este adaptador conecta la capa de dominio (Role) con la capa de infraestructura (RoleEntity en BD).
 * - Implementa el contrato RoleRepository definido en el dominio.
 * - Usa un mapper (RoleMapper) para convertir entre entidades de persistencia y modelos de dominio.
 * - Extiende ReactiveAdapterOperations para reutilizar operaciones genéricas de CRUD.
 */

@Repository
public class RoleReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Role, RoleEntity, Long, RoleReactiveRepository>
        implements RoleRepository {

    private final RoleMapper roleMapper;
    private final RoleReactiveRepository roleReactiveRepository;

    /**
     * Constructor:
     * - Se inyectan el repositorio reactivo de roles y el mapper.
     * - Se pasa el mapper::toModel a la superclase para que convierta de Entity -> Domain automáticamente.
     * - El parámetro "null" indica que no usamos ReactiveCommons en este caso.
     */

    protected RoleReactiveRepositoryAdapter(RoleReactiveRepository roleReactiveRepository, RoleMapper roleMapper){
        super(roleReactiveRepository, null, roleMapper::toModel);
        this.roleReactiveRepository = roleReactiveRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Busca un rol por ID.
     * - Convierte el ID de Long a Integer porque la entidad usa Integer.
     * - Si lo encuentra, lo convierte de entidad a dominio con el mapper.
     */
    @Override
    public Mono<Role> findRoleById(Long id) {
        return roleReactiveRepository.findById(id)
                .map(roleMapper::toModel);
    }


    /**
     * Guarda un nuevo rol en la base de datos.
     * - Convierte de dominio a entidad.
     * - Guarda en la BD.
     * - Convierte la entidad guardada nuevamente a dominio.
     */
    @Override
    public Mono<Role> saveRole(Role role) {
        return roleReactiveRepository.save(roleMapper.toEntity(role))
                .map(roleMapper::toModel);
    }

    /**
     * Actualiza un rol existente.
     * - Primero busca si el rol existe.
     * - Si existe, convierte el nuevo estado a entidad pero conserva el ID original.
     * - Guarda los cambios y retorna el rol actualizado en modelo de dominio.
     */
    @Override
    public Mono<Role> updateRole(Role role) {
        return roleReactiveRepository.findById(role.getId())
                .flatMap(existing -> {
                    RoleEntity entity = roleMapper.toEntity(role);
                    entity.setId(existing.getId()); // conservar ID real
                    return roleReactiveRepository.save(entity);
                })
                .map(roleMapper::toModel);
    }

    /**
     * Elimina un rol por ID.
     * - Convierte el ID de Long a Integer porque la entidad lo maneja así.
     */
    @Override
    public Mono<Void> deleteRole(Long id) {
        return roleReactiveRepository.deleteById(id);
    }

}

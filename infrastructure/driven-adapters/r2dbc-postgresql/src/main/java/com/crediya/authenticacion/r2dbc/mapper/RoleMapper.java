package com.crediya.authenticacion.r2dbc.mapper;

import com.crediya.authenticacion.model.role.Role;
import com.crediya.authenticacion.r2dbc.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", implementationName = "RoleMapperImpl")
public interface RoleMapper {

    @Mapping(target = "id", source = "id")
    RoleEntity toEntity(Role role);

    @Mapping(target = "id", source = "id")
    Role toModel(RoleEntity roleEntity);

    default Long map(Integer value) {
        return value != null ? value.longValue() : null;
    }

    default Integer map(Long value) {
        return value != null ? value.intValue() : null;
    }
}

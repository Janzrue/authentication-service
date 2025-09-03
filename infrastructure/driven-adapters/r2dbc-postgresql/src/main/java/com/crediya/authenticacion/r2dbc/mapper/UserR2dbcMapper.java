package com.crediya.authenticacion.r2dbc.mapper;

import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

/**
 * Mapper de MapStruct para convertir entre:
 * - UserEntity (persistencia, tabla usuarios)
 * - User (modelo de dominio)
 */

@Mapper(componentModel = "spring", implementationName = "UserR2dbcMapperImpl")
public interface UserR2dbcMapper {

    // Convierte UserEntity -> User (dominio)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "idRole", source = "roleId")
    User toModel(UserEntity entity);

    // Convierte User -> UserEntity (persistencia)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "roleId", source = "idRole")
    UserEntity toEntity(User user);

    // Conversión opcional Long <-> BigDecimal
    default BigDecimal map(Long value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

    default Long map(BigDecimal value) {
        return value != null ? value.longValue() : null;
    }
}

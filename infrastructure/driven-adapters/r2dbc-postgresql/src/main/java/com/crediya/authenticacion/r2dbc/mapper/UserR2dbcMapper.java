package com.crediya.authenticacion.r2dbc.mapper;

import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", implementationName = "UserR2dbcMapperImpl")
public interface UserR2dbcMapper {

    @Mapping(target = "idNumber", source = "idUser")
    @Mapping(target = "idRole", source = "roleId")
    User toModel(UserEntity entity);

    @Mapping(target = "idUser", source = "idNumber")
    @Mapping(target = "roleId", source = "idRole")
    UserEntity toEntity(User user);

    default BigDecimal map(Long value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

    default Long map(BigDecimal value) {
        return value != null ? value.longValue() : null;
    }
}

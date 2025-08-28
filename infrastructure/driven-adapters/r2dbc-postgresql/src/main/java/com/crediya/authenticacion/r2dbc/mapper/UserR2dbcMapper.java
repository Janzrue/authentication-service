package com.crediya.authenticacion.r2dbc.mapper;

import com.crediya.authenticacion.model.user.User;
import com.crediya.authenticacion.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", implementationName = "UserR2dbcMapperImpl")
public interface UserR2dbcMapper {

    @Mapping(target = "idNumber", source = "idUser")
    @Mapping(target = "idRole", source = "roleId")
    User toModel(UserEntity entity);

    @Mapping(target = "roleId", source = "idRole")
    UserEntity toEntity(User user);
}

package com.crediya.authenticacion.api.mapper;

import com.crediya.authenticacion.api.dto.UserDTO;
import com.crediya.authenticacion.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.math.BigDecimal;

@Mapper(componentModel = "spring", imports = BigDecimal.class)
public interface UserApiMapper {

    @Mapping(target = "identificationNumber", source = "identificationNumber")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "baseSalary", source = "baseSalary")
    @Mapping(target = "idRole", expression = "java(dto.getRoleId() != null ? BigDecimal.valueOf(dto.getRoleId()) : null)")
    User toDomain(UserDTO dto);

    @Mapping(target = "identificationNumber", source = "identificationNumber")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "baseSalary", source = "baseSalary")
    @Mapping(target = "roleId", expression = "java(Long.valueOf(user.getIdRole() != null ? user.getIdRole().intValue() : null))")
    UserDTO toDTO(User user);
}

package com.crediya.authenticacion.api.mapper;

import com.crediya.authenticacion.api.dto.SaveUserDTO;
import com.crediya.authenticacion.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class UserDTOMapper {

    public User toModel(SaveUserDTO saveUserDTO) {
        return User.builder()
                .name(saveUserDTO.name())
                .lastName(saveUserDTO.lastName())
                .birthDate(saveUserDTO.birthDate())
                .address(saveUserDTO.address())
                .phone(saveUserDTO.phone())
                .email(saveUserDTO.email())
                .baseSalary(saveUserDTO.baseSalary())
                .identificationNumber(saveUserDTO.identificationNumber())
                .build();
    }
}

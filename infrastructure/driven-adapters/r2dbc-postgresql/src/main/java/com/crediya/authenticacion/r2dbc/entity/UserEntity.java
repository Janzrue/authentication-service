package com.crediya.authenticacion.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column( name = "birth_date")
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    @Column(name = "base_salary")
    private Integer baseSalary;
    @Column(name = "identification_number")
    private String identificationNumber;
    @Column(name = "role_id")
    private Long roleId;
}

package com.example.taskproject.common.dto;

import com.example.taskproject.common.enums.UserRole;
import lombok.Getter;

@Getter
public class AuthUserDto {
    private final Long id;
    private final String email;
    private final UserRole userRole;

    public AuthUserDto(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }
}

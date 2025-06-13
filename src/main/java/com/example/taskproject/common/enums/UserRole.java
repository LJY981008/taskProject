package com.example.taskproject.common.enums;

import com.example.taskproject.common.exception.CustomException;

import java.util.Arrays;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return EnumValueOf.fromName(UserRole.class, role, Enum::name);
    }
}

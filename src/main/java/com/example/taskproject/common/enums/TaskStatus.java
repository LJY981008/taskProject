package com.example.taskproject.common.enums;

import com.example.taskproject.common.exception.CustomException;

import java.util.Arrays;

public enum TaskStatus {
    TODO, IN_PROGRESS, DONE, EXPIRATION;

    public static TaskStatus of(String role) {
        return EnumValueOf.fromName(TaskStatus.class, role, Enum::name);
    }
}

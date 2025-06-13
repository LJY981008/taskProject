package com.example.taskproject.common.enums;

public enum TaskPriority {
    LOW, MEDIUM, HIGH;

    public static TaskPriority of(String role) {
        return EnumValueOf.fromName(TaskPriority.class, role, Enum::name);
    }
}

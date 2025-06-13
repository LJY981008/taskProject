package com.example.taskproject.common.enums;

public enum ValidErrorType {
    EMAIL, PASSWORD, USERNAME;

    public static ValidErrorType of(String value){
        return EnumValueOf.fromName(ValidErrorType.class, value, Enum::name);
    }
}

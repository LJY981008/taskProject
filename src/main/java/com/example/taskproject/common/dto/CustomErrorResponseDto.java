package com.example.taskproject.common.dto;

import lombok.Getter;

@Getter
public class CustomErrorResponseDto {
    private final String code;
    private final String message;

    public CustomErrorResponseDto(String code, String message){
        this.code = code;
        this.message = message;
    }
}
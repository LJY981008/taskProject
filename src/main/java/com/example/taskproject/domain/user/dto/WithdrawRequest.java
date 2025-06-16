package com.example.taskproject.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WithdrawRequest {
    @NotBlank
    private String password;

    public WithdrawRequest(String password) {
        this.password = password;
    }
}

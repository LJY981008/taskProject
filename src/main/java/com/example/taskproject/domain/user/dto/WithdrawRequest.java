package com.example.taskproject.domain.user.dto;

import com.example.taskproject.common.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawRequest {
    @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
    private String password;

    public WithdrawRequest(String password) {
        this.password = password;
    }
}

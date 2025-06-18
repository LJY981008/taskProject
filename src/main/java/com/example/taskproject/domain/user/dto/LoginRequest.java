package com.example.taskproject.domain.user.dto;

import com.example.taskproject.common.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = ValidationMessage.USERNAME_REQUIRED)
    private String username;
    @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

package com.example.taskproject.domain.user.dto;

import com.example.taskproject.common.constant.ValidationMessage;
import com.example.taskproject.domain.user.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = ValidationMessage.USERNAME_REQUIRED)
    @Size(min = 4,max = 20)
    private String username;

    @NotBlank(message = ValidationMessage.EMAIL_REQUIRED)
    @Email
    private String email;

    @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
    @ValidPassword
    private String password;

    @NotBlank(message = ValidationMessage.NAME_REQUIRED)
    @Size(min = 2, max = 50)
    private String name;

    public RegisterRequest(String username, String email, String password, String name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
    }
}

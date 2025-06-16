package com.example.taskproject.domain.user.dto;

import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
    private UserRole role;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
}

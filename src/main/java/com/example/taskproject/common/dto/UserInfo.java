package com.example.taskproject.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private String name;
    private String email;
}

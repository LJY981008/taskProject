package com.example.taskproject.domain.user.controller;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.user.dto.LoginRequest;
import com.example.taskproject.domain.user.dto.LoginResponse;
import com.example.taskproject.domain.user.dto.RegisterRequest;
import com.example.taskproject.domain.user.dto.UserResponse;
import com.example.taskproject.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid RegisterRequest request) {
        UserResponse response = userService.register(request);
        return CustomMapper.responseEntity(response, HttpStatus.CREATED, true); // message : 회원가입이 완료되었습니다.
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.login(request);
        return CustomMapper.responseEntity(response, HttpStatus.OK, true); //로그인이 완료되었습니다.
    }

    @GetMapping("/users/me")
    public ResponseEntity<Map<String, Object>> getUser(@AuthenticationPrincipal AuthUserDto userDto) {
        UserResponse response = userService.getUser(userDto);
        return CustomMapper.responseEntity(response, HttpStatus.OK, true); // 사용자 정보를 조회했습니다.
    }
}

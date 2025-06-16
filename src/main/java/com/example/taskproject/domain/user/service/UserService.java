package com.example.taskproject.domain.user.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.CustomErrorCode;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.common.util.JwtUtil;
import com.example.taskproject.common.util.PasswordEncoder;
import com.example.taskproject.domain.user.dto.*;
import com.example.taskproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new CustomException(CustomErrorCode.USERNAME_ALREADY_EXISTS);

        if(userRepository.existsByEmail(request.getEmail()))
            throw new CustomException(CustomErrorCode.EMAIL_ALREADY_EXISTS);

        User user = userRepository.save(new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getUsername(),
                request.getName()
        ));
        return CustomMapper.toDto(user, UserResponse.class);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findUserByUsernameAndDeletedFalse(request.getUsername()).orElseThrow(() -> new CustomException(CustomErrorCode.LOGIN_FAILED, CustomErrorCode.LOGIN_FAILED.getMessage()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(CustomErrorCode.LOGIN_FAILED);
        }

        return new LoginResponse(jwtUtil.createToken(user.getUserId(), user.getEmail(), user.getRole()));
    }

    public UserResponse getUser(AuthUserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        return CustomMapper.toDto(user, UserResponse.class);
    }

    @Transactional
    public void withdraw(WithdrawRequest request, AuthUserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(CustomErrorCode.PASSWORD_MISMATCH);
        }
        userRepository.softDeleteById(user.getUserId());
    }

}

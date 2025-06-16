package com.example.taskproject.domain.user.service;

import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.CustomErrorCode;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.common.util.PasswordEncoder;
import com.example.taskproject.domain.user.dto.RegisterRequest;
import com.example.taskproject.domain.user.dto.UserResponse;
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

}

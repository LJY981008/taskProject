package com.example.taskproject.domain.user.service;

import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.common.util.PasswordEncoder;
import com.example.taskproject.domain.user.dto.RegisterRequest;
import com.example.taskproject.domain.user.dto.UserResponse;
import com.example.taskproject.domain.user.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private MockedStatic<CustomMapper> mockStatic;
    private RegisterRequest request;

    @BeforeEach
    void init() {
        mockStatic = Mockito.mockStatic(CustomMapper.class);
        request = new RegisterRequest("test", "test@naver.com", "Qwer!234", "test-name");

    }

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        //given
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.FALSE);
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(Boolean.FALSE);
        mockStatic.when(() -> CustomMapper.toDto(Mockito.any(User.class), Mockito.eq(UserResponse.class))).thenReturn(Mockito.mock(UserResponse.class));

        // when
        userService.register(request);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        mockStatic.close();
    }

    @Test
    @DisplayName("회원가입 시 중복 아이디로 실패")
    void 회원가입_실패_중복아이디() {
        //given
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.TRUE);

        //when + then
        CustomException exception = assertThrows(CustomException.class, () -> userService.register(request));
        Assertions.assertEquals(exception.getCustomMessage(),"이미 존재하는 사용자명입니다");
        mockStatic.close();
    }

    @Test
    @DisplayName("회원가입 시 중복 이메일로 실패")
    void 회원가입_실패_중복이메일() {
        // given
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(Boolean.TRUE);

        //when + then
        CustomException exception = assertThrows(CustomException.class, () -> userService.register(request));
        Assertions.assertEquals(exception.getCustomMessage(),"이미 존재하는 이메일입니다.");
        mockStatic.close();
    }


}
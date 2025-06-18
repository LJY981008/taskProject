package com.example.taskproject.domain.user.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.UserRole;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.common.util.JwtUtil;
import com.example.taskproject.common.util.PasswordEncoder;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import com.example.taskproject.domain.user.dto.LoginRequest;
import com.example.taskproject.domain.user.dto.RegisterRequest;
import com.example.taskproject.domain.user.dto.UserResponse;
import com.example.taskproject.domain.user.dto.WithdrawRequest;
import com.example.taskproject.domain.user.repository.UserRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ActiveLogService activeLogService;
    @InjectMocks
    private UserService userService;

    private MockedStatic<CustomMapper> mockStatic;

    @BeforeEach
    void init() {
        mockStatic = Mockito.mockStatic(CustomMapper.class);
    }

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        //given
        RegisterRequest request = new RegisterRequest("test", "test@naver.com", "Qwer!234", "test-name");
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.FALSE);
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(Boolean.FALSE);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "userId", 1L);
            return user;
        });
        mockStatic.when(() -> CustomMapper.toDto(Mockito.any(User.class), Mockito.eq(UserResponse.class))).thenReturn(Mockito.mock(UserResponse.class));

        // when
        userService.register(request);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(activeLogService, Mockito.times(1)).logActivity(1L, "USER_REGISTER", 1L);
        mockStatic.close();
    }

    @Test
    @DisplayName("회원가입 시 중복 아이디로 실패")
    void 회원가입_실패_중복아이디() {
        //given
        RegisterRequest request = new RegisterRequest("test", "test@naver.com", "Qwer!234", "test-name");
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.TRUE);

        //when + then
        CustomException exception = assertThrows(CustomException.class, () -> userService.register(request));
        Assertions.assertEquals("이미 존재하는 사용자명입니다", exception.getCustomMessage());
        mockStatic.close();
    }

    @Test
    @DisplayName("회원가입 시 중복 이메일로 실패")
    void 회원가입_실패_중복이메일() {
        // given
        RegisterRequest request = new RegisterRequest("test", "test@naver.com", "Qwer!234", "test-name");
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(Boolean.TRUE);

        //when + then
        CustomException exception = assertThrows(CustomException.class, () -> userService.register(request));
        Assertions.assertEquals("이미 존재하는 이메일입니다.", exception.getCustomMessage());
        mockStatic.close();
    }

    @Test
    @DisplayName("로그인 성공")
    void 로그인_성공() {
        //given
        User user = Mockito.mock(User.class);
        LoginRequest request = new LoginRequest("test", "Qwer!234");
        Mockito.when(userRepository.findUserByUsernameAndDeletedFalse(request.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(Boolean.TRUE);

        //when
        userService.login(request);

        //then
        Mockito.verify(jwtUtil, Mockito.times(1)).createToken(user.getUserId(), user.getEmail(), user.getRole());
        mockStatic.close();

    }

    @Test
    @DisplayName("틀린 아이디로 로그인 실패")
    void 로그인_실패_틀린아이디() {
        //given
        LoginRequest request = new LoginRequest("test", "Qwer!234");
        Mockito.when(userRepository.findUserByUsernameAndDeletedFalse(request.getUsername())).thenReturn(Optional.empty());

        //when + then
        CustomException exception = assertThrows(CustomException.class, () -> userService.login(request));
        Assertions.assertEquals("잘못된 사용자명 또는 비밀번호입니다", exception.getCustomMessage());
        mockStatic.close();

    }

    @Test
    @DisplayName("틀린 비밀번호로 로그인 실패")
    void 로그인_실패_틀린_비밀번호() {
        //given
        User user = Mockito.mock(User.class);
        LoginRequest request = new LoginRequest("test", "Qwer!234");
        Mockito.when(userRepository.findUserByUsernameAndDeletedFalse(request.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(Boolean.FALSE);

        //when + then
        CustomException exception = assertThrows(CustomException.class, () -> userService.login(request));
        Assertions.assertEquals("잘못된 사용자명 또는 비밀번호입니다", exception.getCustomMessage());
        mockStatic.close();

    }

    @Test
    @DisplayName("회원 조회 실패")
    void 회원_조회_실패() {
        //given
        AuthUserDto userDto = new AuthUserDto(1L, "test@naver.com", UserRole.USER);
        Mockito.when(userRepository.findByUserIdAndDeletedFalse(userDto.getId())).thenReturn(Optional.empty());

        //when+ then
        CustomException exception = assertThrows(CustomException.class, () -> userService.getUser(userDto));
        Assertions.assertEquals("존재하지 않거나 탈퇴한 유저입니다.", exception.getCustomMessage());
        mockStatic.close();
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void 회원_탈퇴_성공() {
        User user = Mockito.mock(User.class);
        AuthUserDto userDto = Mockito.mock(AuthUserDto.class);
        WithdrawRequest request = new WithdrawRequest("Qwer!234");
        Mockito.when(userRepository.findByUserIdAndDeletedFalse(userDto.getId())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(Boolean.TRUE);
        // when + then
        userService.withdraw(request, userDto);
        Mockito.verify(user,Mockito.times(1)).delete();
        mockStatic.close();
    }

    @Test
    @DisplayName("회원 탈퇴 시 비밀번호 불일치")
    void 회원탈퇴_실패_비밀번호_불일치() {

        //given
        User user = Mockito.mock(User.class);
        AuthUserDto userDto = Mockito.mock(AuthUserDto.class);
        WithdrawRequest request = new WithdrawRequest("Qwer!234");
        Mockito.when(userRepository.findByUserIdAndDeletedFalse(userDto.getId())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(Boolean.FALSE);

        // when + then
        CustomException exception = assertThrows(CustomException.class, () -> userService.withdraw(request, userDto));
        Assertions.assertEquals("비밀번호가 일치하지 않습니다.", exception.getCustomMessage());
        mockStatic.close();
    }
}
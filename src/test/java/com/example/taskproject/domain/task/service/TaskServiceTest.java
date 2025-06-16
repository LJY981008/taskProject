package com.example.taskproject.domain.task.service;
import com.example.taskproject.common.dto.TaskUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.dto.TaskCreateRequestDto;
import com.example.taskproject.common.dto.TaskResponseDto;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.TaskPriority;
import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.user.repository.UserRepository;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;


import java.time.LocalDateTime;
import java.util.Optional;


import static com.example.taskproject.common.enums.UserRole.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private AuthUserDto authUserDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authUserDto = new AuthUserDto(1L, "test@example.com", USER);
    }

    @Test
    public void 태스크생성() {
        // given
        TaskCreateRequestDto request = new TaskCreateRequestDto();
        request.setTitle("태스크서비스 테스트");
        request.setTaskPriority(TaskPriority.HIGH);
        request.setTaskStatus(TaskStatus.TODO);
        request.setDeadline(LocalDateTime.now().plusDays(1));
        request.setManagerId(null);

        User author = new User();
        author.setUserId(authUserDto.getId());

        when(userRepository.findById(authUserDto.getId())).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TaskResponseDto response = taskService.createTask(request, authUserDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("태스크서비스 테스트");
        assertThat(response.getTaskPriority()).isEqualTo(TaskPriority.HIGH);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void 테스크수정() {
        // given
        Long taskId = 1L;
        TaskUpdateRequestDto request = new TaskUpdateRequestDto();
        request.setTitle("업데이트 테스트");
        request.setTaskStatus(TaskStatus.IN_PROGRESS);

        User author = new User();
        author.setUserId(authUserDto.getId());

        Task existingTask = new Task();
        existingTask.setTaskId(taskId);
        existingTask.setAuthor(author);
        existingTask.setTaskStatus(TaskStatus.TODO);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        // when
        TaskResponseDto response = taskService.updateTask(taskId, request, authUserDto);

        // then
        assertThat(response.getTitle()).isEqualTo("업데이트 테스트");
        assertThat(response.getTaskStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }


}
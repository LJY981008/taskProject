package com.example.taskproject.domain.task.service;
import com.example.taskproject.common.dto.TaskUpdateRequestDto;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static com.example.taskproject.common.enums.UserRole.USER;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActiveLogService activeLogService;

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
        request.setPriority(TaskPriority.HIGH);
        request.setTaskStatus(TaskStatus.TODO);
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setAssigneeId(null);

        User author = mock(User.class);
        ReflectionTestUtils.setField(author, "userId", 1L);

        when(userRepository.findById(authUserDto.getId())).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TaskResponseDto response = taskService.createTask(request, authUserDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("태스크서비스 테스트");
        assertThat(response.getPriority()).isEqualTo(TaskPriority.HIGH);
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(activeLogService, times(1)).logActivity(1L, "TASK_CREATED", null);
    }

    @Test
    public void 태스크수정() {
        // given
        Long taskId = 1L;
        TaskUpdateRequestDto request = new TaskUpdateRequestDto();
        request.setTitle("업데이트 테스트");
        request.setTaskStatus(TaskStatus.IN_PROGRESS);

        User author = mock(User.class);

        Task existingTask = new Task();
        existingTask.setTaskId(taskId);
        existingTask.setAuthor(author);
        existingTask.setTaskStatus(TaskStatus.TODO);

        existingTask.setAuthor(author);
        when(author.getUserId()).thenReturn(1L);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        // when
        TaskResponseDto response = taskService.updateTask(taskId, request, authUserDto);

        // then
        assertThat(response.getTitle()).isEqualTo("업데이트 테스트");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(activeLogService, times(1)).logActivity(author.getUserId(), "TASK_UPDATED", taskId);
    }

    @Test
    public void 태스크단건조회() {
        // given
        Long taskId = 1L;

        User author = mock(User.class);
        ReflectionTestUtils.setField(author, "userId", 1L);

        Task task = new Task();
        task.setTaskId(taskId);
        task.setTitle("테스트 태스크");
        task.setAuthor(author);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // when
        TaskResponseDto response = taskService.getTask(taskId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("테스트 태스크");

    }

    @Test
    public void 태스크전체조회(){
        // given
        Pageable pageable = Pageable.ofSize(10);

        User author = mock(User.class);
        ReflectionTestUtils.setField(author, "userId", 1L);

        Task task = new Task();
        task.setTaskId(1L);
        task.setTitle("태스크전체조회테스트");
        task.setAuthor(author);

        Page<Task> page = new PageImpl<Task>(List.of(task), pageable, 1);
        when(taskRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<TaskResponseDto> responsePage = taskService.getAllTasks(pageable);

        // then
        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0).getTitle()).isEqualTo("태스크전체조회테스트");

    }

    @Test
    public void 태스크삭제(){
        // given
        Long taskId = 1L;
        Task task = new Task();
        task.setTaskId(taskId);

        User author = mock(User.class);
        when(author.getUserId()).thenReturn(1L);
        task.setAuthor(author);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.deleteTask(taskId, authUserDto);

        // then
        verify(taskRepository, times(1)).save(task);
        assertThat(task.isDeleted()).isTrue();
        assertThat(task.getDeletedAt()).isNotNull();
        verify(activeLogService, times(1)).logActivity(author.getUserId(), "TASK_DELETED", taskId);
    }


}
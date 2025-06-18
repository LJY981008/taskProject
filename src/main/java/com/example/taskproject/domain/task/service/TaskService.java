package com.example.taskproject.domain.task.service;


import com.example.taskproject.common.annotation.Logging;
import com.example.taskproject.common.dto.*;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.ActivityType;
import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import com.example.taskproject.domain.task.exception.TaskNotFoundException;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.user.exception.UserNotFoundException;
import com.example.taskproject.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.taskproject.common.enums.CustomErrorCode.UNAUTHENTICATED;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ActiveLogService activeLogService;


    // 태스크 생성
    @Logging(ActivityType.TASK_CREATED)
    @Transactional
    public TaskResponseDto createTask(TaskCreateRequestDto request, AuthUserDto userDto) {
        User author = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("작성자를 찾을수 없습니다"));

        User manager = null;
        if(request.getAssigneeId() != null) {
            manager = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 담당자 입니다"));
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setContents(request.getDescription());
        task.setTaskPriority(request.getPriority());
        task.setTaskStatus(request.getTaskStatus() != null ? request.getTaskStatus() : TaskStatus.TODO);
        task.setDueDate(request.getDueDate().atStartOfDay());
        task.setStartedAt(LocalDateTime.now());
        task.setAuthor(author);
        task.setManager(manager);

        Task saved = taskRepository.save(task);


        return CustomMapper.toDto(saved, TaskResponseDto.class);

    }

    // 태스크 수정
    @Logging(ActivityType.TASK_UPDATED)
    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskUpdateRequestDto request, AuthUserDto userDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if(!task.getAuthor().getUserId().equals(userDto.getId())) {
            throw new CustomException(UNAUTHENTICATED);
        }

        User manager = Optional.ofNullable(request.getAssigneeId()).map(id->userRepository.findByUserIdAndDeletedFalse(id)
                        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 담당자 입니다")))
                .orElse(null);

        task.update(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDueDate(),
                manager
        );

        if(request.getStatus() != null && !request.getStatus().equals(task.getTaskStatus())) {
            task.setTaskStatus(request.getStatus());
            if (request.getStatus() == TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
                task.setStartedAt(LocalDateTime.now());
            }
        }
        Task saved = taskRepository.save(task);
        //activeLogService.logActivity(userDto.getId(), "TASK_UPDATED", task.getTaskId());

        return CustomMapper.toDto(saved, TaskResponseDto.class);
    }




    // 테스크 상태 수정
    @Transactional
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatusUpdateRequest request, AuthUserDto userDto) {
        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if(!task.getAuthor().getUserId().equals(userDto.getId())) {
            throw new CustomException(UNAUTHENTICATED);
        }

        task.setTaskStatus(request.getStatus());

        Task save = taskRepository.save(task);
        activeLogService.logActivity(userDto.getId(), "TASK_STATUS_UPDATED", task.getTaskId());

        return CustomMapper.toDto(save, TaskResponseDto.class);
    }

    // 태스크 전체 조회
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getAllTasks(
            Pageable pageable,
            TaskStatus status,
            String search,
            long assigneeId
    ){
        Page<Task> tasks = taskRepository.findByFilterTask(pageable, status, search, assigneeId);

        return tasks.map(TaskResponseDto::new);
    }

    // 태스크 단건 조회
    @Transactional(readOnly = true)
    public TaskResponseDto getTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
        return CustomMapper.toDto(task, TaskResponseDto.class);
    }

    // 태스크 삭제(soft delete)
    @Logging(ActivityType.TASK_DELETED)
    @Transactional
    public void deleteTask(Long taskId, AuthUserDto userDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        if (!task.getAuthor().getUserId().equals(userDto.getId())) {
            throw new CustomException(UNAUTHENTICATED);
        }

        task.delete();

        taskRepository.save(task);
    }

}

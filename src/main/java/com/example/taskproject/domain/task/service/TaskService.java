package com.example.taskproject.domain.task.service;


import com.example.taskproject.common.dto.*;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.taskproject.common.enums.CustomErrorCode.TASK_NOT_FOUND;
import static com.example.taskproject.common.enums.CustomErrorCode.UNAUTHENTICATED;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ActiveLogService activeLogService;


    // 태스크 생성
    @Transactional
    public TaskResponseDto createTask(TaskCreateRequestDto request, AuthUserDto userDto) {
        User author = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("작성자를 찾을수 없습니다"));

        User manager = null;
        if(request.getAssigneeId() != null) {
            manager = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 담당자 입니다"));
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setContents(request.getDescription());
        task.setTaskPriority(request.getPriority());
        task.setTaskStatus(request.getTaskStatus() != null ? request.getTaskStatus() : TaskStatus.TODO);
        task.setDueDate(request.getDueDate().atStartOfDay());

        task.setStartedAt(LocalDateTime.now());

//        if (task.getTaskStatus() == TaskStatus.IN_PROGRESS) {
//            task.setStartedAt(LocalDateTime.now());
//        } else {
//            task.setStartedAt(null);
//        }

        task.setAuthor(author);
        task.setManager(manager);

        Task saved = taskRepository.save(task);

        activeLogService.logActivity(userDto.getId(), "TASK_CREATED", task.getTaskId());

        return new TaskResponseDto(saved);

    }

    // 태스크 수정
    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskUpdateRequestDto request, AuthUserDto userDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테스크 입니다"));

        if(!task.getAuthor().getUserId().equals(userDto.getId())) {
            throw new CustomException(UNAUTHENTICATED);
        }

        if(request.getTitle() != null && !request.getTitle().isBlank()) {
            task.setTitle(request.getTitle());
        }
        if(request.getDescription() != null) {
            task.setContents(request.getDescription());
        }
        if(request.getPriority() != null) {
            task.setTaskPriority(request.getPriority());
        }
        if(request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if(request.getAssigneeId() != null) {
            User manager = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 담당자 입니다"));
            task.setManager(manager);
        }
        if(request.getStatus() != null && !request.getStatus().equals(task.getTaskStatus())) {
            task.setTaskStatus(request.getStatus());
            if (request.getStatus() == TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
                task.setStartedAt(LocalDateTime.now());
            }
        }
        Task saved = taskRepository.save(task);
        activeLogService.logActivity(userDto.getId(), "TASK_UPDATED", task.getTaskId());

        return new TaskResponseDto(saved);
    }

    // 테스크 상태 수정
    @Transactional
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatusUpdateRequest request, AuthUserDto userDto) {
        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new CustomException(TASK_NOT_FOUND));

        if(!task.getAuthor().getUserId().equals(userDto.getId())) {
            throw new CustomException(UNAUTHENTICATED);
        }

        task.setTaskStatus(request.getStatus());

        Task save = taskRepository.save(task);
        activeLogService.logActivity(userDto.getId(), "TASK_STATUS_UPDATED", task.getTaskId());

        return new TaskResponseDto(save);
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
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 태스크 입니다."));
        return new TaskResponseDto(task);
    }

    // 태스크 삭제(soft delete)
    @Transactional
    public void deleteTask(Long taskId, AuthUserDto userDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 태스크입니다."));

        if (!task.getAuthor().getUserId().equals(userDto.getId())) {
            throw new CustomException(UNAUTHENTICATED);
        }
        task.setDeleted(true);
        task.setDeletedAt(LocalDateTime.now());
        activeLogService.logActivity(userDto.getId(), "TASK_DELETED", task.getTaskId());

        taskRepository.save(task);
    }

}

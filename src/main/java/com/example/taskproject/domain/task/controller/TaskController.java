package com.example.taskproject.domain.task.controller;


import com.example.taskproject.common.dto.*;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // 태스크 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTask(
            @RequestBody @Valid TaskCreateRequestDto request,
            @AuthenticationPrincipal AuthUserDto userDto) {
        TaskResponseDto response = taskService.createTask(request, userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomMapper.responseToMap(response, true));
    }

    // 태스크 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<Map<String, Object>> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskUpdateRequestDto request,
            @AuthenticationPrincipal AuthUserDto userDto
    ){
        TaskResponseDto response = taskService.updateTask(taskId, request, userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(response, true));
    }

    @PutMapping("{taskId}/status")
    public ResponseEntity<Map<String, Object>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskStatusUpdateRequest request,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        TaskResponseDto response = taskService.updateTaskStatus(taskId, request, userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(response, true));
    }

    // 태스크 단건 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<Map<String,Object>> getTask(@PathVariable Long taskId) {
        TaskResponseDto response = taskService.getTask(taskId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(response, true));
    }

    // 태스크 전체 조회
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllTasks(
            Pageable pageable,
            @RequestParam TaskStatus status,
            @RequestParam(defaultValue = "") String search,
            @RequestParam Optional<Long> assigneeId,
            @AuthenticationPrincipal AuthUserDto userDto

    ) {
        long assId = assigneeId.orElse(userDto.getId());
        Page<TaskResponseDto> tasks = taskService.getAllTasks(pageable, status, search, assId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(tasks, true));
    }

    // 태스크 삭제(soft delete)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Map<String,Object>> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        taskService.deleteTask(taskId, userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(null, true));
    }

}

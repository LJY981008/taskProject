package com.example.taskproject.domain.task.controller;


import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.dto.TaskCreateRequestDto;
import com.example.taskproject.common.dto.TaskResponseDto;
import com.example.taskproject.common.dto.TaskUpdateRequestDto;
import com.example.taskproject.common.entity.Task;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // 태스크 생성
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @RequestBody @Valid TaskCreateRequestDto request,
            @AuthenticationPrincipal AuthUserDto userDto) {
        TaskResponseDto response = taskService.createTask(request, userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 태스크 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskUpdateRequestDto request,
            @AuthenticationPrincipal AuthUserDto userDto){
        TaskResponseDto response = taskService.updateTask(taskId, request, userDto);
        return ResponseEntity.ok(response);
    }

    // 태스크 단건 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long taskId) {
        TaskResponseDto response = taskService.getTask(taskId);
        return ResponseEntity.ok(response);
    }

    // 태스크 전체 조회
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getAllTasks(pageable);
        return ResponseEntity.ok(tasks);
    }

    // 태스크 삭제(soft delete)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal AuthUserDto userDto
    ) {
        taskService.deleteTask(taskId, userDto);
        return ResponseEntity.noContent().build();
    }

}

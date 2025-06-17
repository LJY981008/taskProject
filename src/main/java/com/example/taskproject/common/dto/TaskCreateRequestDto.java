package com.example.taskproject.common.dto;

import com.example.taskproject.common.enums.TaskPriority;
import com.example.taskproject.common.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequestDto {

    @NotBlank(message = "title을 입력해주세요")
    private String title;

    private String description;

    @NotNull(message = "taskpriority를 입력해주세요")
    private TaskPriority priority;

    private Long assigneeId;

    @FutureOrPresent(message = "deadline은 현재 또는 미래여야 합니다.")
    private LocalDate dueDate;

    private TaskStatus taskStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;



}

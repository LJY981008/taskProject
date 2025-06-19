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

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private TaskPriority priority;

    private Long assigneeId;

    @FutureOrPresent
    private LocalDate dueDate;

    private TaskStatus taskStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;



}

package com.example.taskproject.common.dto;


import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.TaskPriority;
import com.example.taskproject.common.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
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
public class TaskUpdateRequestDto {
    private long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;
    private Long assigneeId;
    private UserInfo assignee;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}

package com.example.taskproject.common.dto;


import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.TaskPriority;
import com.example.taskproject.common.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponseDto {

    private Long id;

    private String title;

    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    private Long assigneeId;

    private UserInfo assignee;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public TaskResponseDto(Task task) {
        this.id = task.getTaskId();
        this.title = task.getTitle();
        this.description = task.getContents();
        this.priority = task.getTaskPriority();
        this.status = task.getTaskStatus();
        this.dueDate = task.getDueDate();
        this.startedAt = task.getStartedAt();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getModifiedAt();
        User author = task.getAuthor();
        this.assigneeId = author.getUserId();
        this.assignee = new UserInfo(author.getUserId(), author.getUsername(), author.getName(), author.getEmail());
    }

    public TaskResponseDto(Long id, String title, String description, TaskPriority priority, TaskStatus status, LocalDateTime dueDate, LocalDateTime startedAt, Long assigneeId, UserInfo assignee, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.startedAt = startedAt;
        this.assigneeId = assigneeId;
        this.assignee = assignee;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

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
@AllArgsConstructor
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String name;
        private String email;
    }

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
}

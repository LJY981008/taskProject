package com.example.taskproject.common.dto;


import com.example.taskproject.common.entity.Task;
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

    private String contents;

    private TaskPriority taskPriority;

    private TaskStatus taskStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    private UserInfo author;

    private UserInfo manager;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    private boolean deleted;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String name;
    }

    public TaskResponseDto(Task task) {
        this.id = task.getTaskId();
        this.title = task.getTitle();
        this.contents = task.getContents();
        this.taskPriority = task.getTaskPriority();
        this.taskStatus = task.getTaskStatus();
        this.deadline = task.getDeadline();
        this.startedAt = task.getStartedAt();
        this.createdAt = task.getCreatedAt();
        this.modifiedAt = task.getModifiedAt();
        this.deleted = task.isDeleted();
        this.author = new UserInfo(task.getAuthor().getUserId(), task.getAuthor().getUsername());
        if (task.getManager() != null) {
            this.manager = new UserInfo(task.getManager().getUserId(), task.getManager().getUsername());
        }
    }
}

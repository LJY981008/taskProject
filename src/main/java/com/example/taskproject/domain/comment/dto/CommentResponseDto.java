package com.example.taskproject.domain.comment.dto;

import com.example.taskproject.common.dto.TaskResponseDto;
import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final Long taskId;
    private final Long userId;
    private final TaskResponseDto.UserInfo user;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment){
        this.id = comment.getCommentId();
        this.content = comment.getContents();
        this.taskId = comment.getTask().getTaskId();
        this.userId = comment.getAuthor().getUserId();
        User author = comment.getAuthor();
        this.user = new TaskResponseDto.UserInfo(author.getUserId(), author.getUsername(), author.getName(), author.getEmail());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getModifiedAt();
    }
}

package com.example.taskproject.domain.comment.dto;

import com.example.taskproject.common.dto.UserInfo;
import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final Long taskId;
    private final Long userId;
    private final UserInfo user;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment){
        this.id = comment.getCommentId();
        this.content = comment.getContents();
        this.taskId = comment.getTask().getTaskId();
        this.userId = comment.getAuthor().getUserId();
        User author = comment.getAuthor();
        this.user = new UserInfo(author.getUserId(), author.getUsername(), author.getName(), author.getEmail());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getModifiedAt();
    }
}

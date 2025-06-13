package com.example.taskproject.domain.comment.dto;

import com.example.taskproject.common.entity.Comment;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreateCommentResponseDto {
    private final Long commentId;
    private final Long authorId;
    private final String contents;
    private final Long taskId;
    private final LocalDateTime createdAt;

    public CreateCommentResponseDto(Long commentId, Long authorId, String contents, Long taskId){
        this.commentId = commentId;
        this.authorId = authorId;
        this.contents = contents;
        this.taskId = taskId;
        this.createdAt = LocalDateTime.now();
    }

    public CreateCommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.authorId = comment.getAuthor().getUserId();
        this.contents = comment.getContents();
        this.taskId = comment.getTask().getTaskId();
        this.createdAt = LocalDateTime.now();
    }



}

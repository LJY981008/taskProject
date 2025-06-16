package com.example.taskproject.domain.comment.dto;

import com.example.taskproject.common.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateCommentResponseDto {

    private final Long commentId;
    private final Long authorId;
    private final String contents;
    private final Long taskId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateCommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.authorId = comment.getAuthor().getUserId();
        this.contents = comment.getContents();
        this.taskId = comment.getTask().getTaskId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

}

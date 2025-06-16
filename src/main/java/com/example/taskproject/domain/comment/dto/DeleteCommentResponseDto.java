package com.example.taskproject.domain.comment.dto;

import com.example.taskproject.common.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteCommentResponseDto {

    private final Long commentId;
    private final Long authorId;
    private final String contents;
    private final Long taskId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final boolean deleted;
    private final LocalDateTime deletedAt;

    public DeleteCommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.authorId = comment.getAuthor().getUserId();
        this.contents = comment.getContents();
        this.taskId = comment.getTask().getTaskId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.deleted = comment.isDeleted();
        this.deletedAt = comment.getDeletedAt();
    }
}

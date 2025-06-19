package com.example.taskproject.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequestDto(@NotBlank String content) {
    public UpdateCommentRequestDto(String content) {
        this.content = content;
    }
}

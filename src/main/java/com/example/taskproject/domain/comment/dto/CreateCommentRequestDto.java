package com.example.taskproject.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;


public record CreateCommentRequestDto(@NotBlank String content) {
}

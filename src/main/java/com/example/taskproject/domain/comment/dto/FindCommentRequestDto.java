package com.example.taskproject.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;


public record FindCommentRequestDto(@NotBlank String content) {
}

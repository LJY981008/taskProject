package com.example.taskproject.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

import static com.example.taskproject.common.constant.ValidationMessage.COMMENT_CONTENTS;

public record CreateCommentRequestDto(@NotBlank(message = COMMENT_CONTENTS) String content) {
}

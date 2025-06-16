package com.example.taskproject.domain.comment.dto;

import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class UpdateCommentRequestDto {
    @Valid
    private final String contents;

    public UpdateCommentRequestDto(String contents){
        this.contents = contents;
    }
}

package com.example.taskproject.domain.comment.dto;

import lombok.Getter;

@Getter
public class UpdateCommentRequestDto {
    private final String contents;

    public UpdateCommentRequestDto(String contents){
        this.contents = contents;
    }
}

package com.example.taskproject.domain.comment.dto;

import lombok.Getter;

@Getter
public class FindCommentRequestDto {
    private final String contents;

    public FindCommentRequestDto(String contents){
        this.contents = contents;
    }
}

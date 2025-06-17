package com.example.taskproject.domain.comment.dto;

import lombok.Getter;

@Getter
public class FindCommentRequestDto {
    private final String content;

    public FindCommentRequestDto(String content){
        this.content = content;
    }
}

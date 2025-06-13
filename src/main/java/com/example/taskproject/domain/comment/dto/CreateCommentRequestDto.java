package com.example.taskproject.domain.comment.dto;

import lombok.Getter;

@Getter
public class CreateCommentRequestDto {

    private final String contents;


    public CreateCommentRequestDto(String contents){
        this.contents = contents;
    }
}

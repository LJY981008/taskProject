package com.example.taskproject.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentRequestDto {
    @NotBlank
    private final String contents;


    public CreateCommentRequestDto(String contents){
        this.contents = contents;
    }
}

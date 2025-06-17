package com.example.taskproject.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import static com.example.taskproject.common.constant.ValidationMessage.COMMENT_CONTENTS;

@Getter
public class CreateCommentRequestDto {
    @NotBlank(message = COMMENT_CONTENTS)
    private final String contents;


    public CreateCommentRequestDto(String contents){
        this.contents = contents;
    }
}

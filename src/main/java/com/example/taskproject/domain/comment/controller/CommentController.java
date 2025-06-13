package com.example.taskproject.domain.comment.controller;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.domain.comment.dto.CreateCommentRequestDto;
import com.example.taskproject.domain.comment.dto.CreateCommentResponseDto;
import com.example.taskproject.domain.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }



    @PostMapping
    public ResponseEntity<CreateCommentResponseDto> createComment(
            @PathVariable Long taskId,
            @RequestBody CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto){

        CreateCommentResponseDto responseDto = commentService.createComment(taskId, requestDto, userDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

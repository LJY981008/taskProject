package com.example.taskproject.domain.comment.controller;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.domain.comment.dto.*;
import com.example.taskproject.domain.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글 기능의 HTTP 요청을 처리하는 컨트롤러
 *
 * @author 이현하
 */
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

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }



    @PutMapping("/{commentId}")
    public ResponseEntity<UpdateCommentResponseDto> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto){

        UpdateCommentResponseDto responseDto = commentService.updateComment(taskId, commentId, requestDto, userDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<DeleteCommentResponseDto> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal AuthUserDto userDto){

        DeleteCommentResponseDto responseDto = commentService.deleteComment(taskId, commentId, userDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }
}

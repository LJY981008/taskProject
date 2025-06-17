package com.example.taskproject.domain.comment.controller;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.comment.dto.*;
import com.example.taskproject.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 댓글 기능의 HTTP 요청을 처리하는 컨트롤러
 *
 * @author 이현하
 */
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }


    /**
     * <p>댓글 생성</p>
     *
     * @param taskId     태스크 id
     * @param requestDto 요청 dto
     * @param userDto    로그인한 사용자 dto
     * @return CreateCommentResponseDto 생성된 댓글 응답 dto
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(
            @PathVariable Long taskId,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto){

        CommentResponseDto responseDto = commentService.createComment(taskId, requestDto, userDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomMapper.responseToMap(responseDto, true));
    }


    /**
     * 댓글 전체 조회
     *
     * @param taskId 태스크 id
     * @return List<FindCommentResponseDto> 댓글 조회 응답 dto 리스트
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllComment(
            @PathVariable Long taskId){

        List<FindCommentResponseDto> responseDto = commentService.findAll(taskId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(responseDto, true));
    }


    /**
     * 댓글 내용으로 댓글 검색
     *
     * @param taskId     태스크 id
     * @param requestDto 요청 dto
     * @return List<FindCommentResponseDto> 댓글 조회 응답 dto 리스트
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> findByContents(
            @PathVariable Long taskId,
            @RequestBody FindCommentRequestDto requestDto){

        List<FindCommentResponseDto> responseDto = commentService.findByContents(taskId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(responseDto, true));
    }


    /**
     * <p>댓글 수정</p>
     *
     * @param taskId     태스크 id
     * @param commentId  댓글 id
     * @param requestDto 요청 dto
     * @param userDto    로그인된 사용자 dto
     * @return UpdateCommentResponseDto 수정된 댓글 응답 dto
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDto userDto){

        CommentResponseDto responseDto = commentService.updateComment(taskId, commentId, requestDto, userDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(responseDto, true));
    }


    /**
     * <p>댓글 삭제</p>
     *
     * @param taskId    태스크 id
     * @param commentId 요청 dto
     * @param userDto   로그인된 사용자 dto
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal AuthUserDto userDto){

        commentService.deleteComment(taskId, commentId, userDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(null, true));
    }
}

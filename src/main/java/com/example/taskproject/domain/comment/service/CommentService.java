package com.example.taskproject.domain.comment.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.domain.comment.dto.*;
import com.example.taskproject.domain.comment.repository.CommentRepository;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.taskproject.common.enums.CustomErrorCode.*;

/**
 * 댓글 생성, 조회, 수정, 삭제 기능 클래스
 *
 * @author 이현하
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    /**
     * <p>댓글 생성</p>
     *
     * @param taskId 태스크 id
     * @param requestDto 요청 dto
     * @param userDto 로그인된 사용자 dto
     * @return CreateCommentResponseDto 생성된 댓글 응답 dto
     */
    @Transactional
    public CreateCommentResponseDto createComment(
            Long taskId,
            CreateCommentRequestDto requestDto,
            AuthUserDto userDto){


        User user = userRepository.findUserByEmailAndDeletedFalse(userDto.getEmail()).orElseThrow(() -> new CustomException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        Task task = taskRepository.findTaskByTaskIdAndDeletedFalse(taskId).orElseThrow(() -> new CustomException(TASK_NOT_FOUND, TASK_NOT_FOUND.getMessage()));

        // 요청 dto에 댓글 내용이 입력되지 않을 경우 예외 처리
        if(requestDto.getContents() == null || requestDto.getContents().isBlank()){
            throw new CustomException(COMMENT_NOT_ENTERED, COMMENT_NOT_ENTERED.getMessage());
        }

        Comment comment = new Comment(requestDto.getContents(), user, task);
        commentRepository.save(comment);

        return new CreateCommentResponseDto(comment);
    }


    /**
     * 댓글 전체 조회
     *
     * @param taskId 태스크 id
     * @return List<FindCommentResponseDto> 댓글 조회 응답 dto 리스트
     */
    @Transactional
    public List<FindCommentResponseDto> findAll(Long taskId){

        List<Comment> commentList = commentRepository.findByTask_TaskIdAndDeletedFalse(taskId);

        if (commentList == null || commentList.isEmpty()) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }

        return commentList
                .stream()
                .map(FindCommentResponseDto::new)
                .collect(Collectors.toList());

    }


    /**
     * 댓글 내용으로 댓글 검색
     *
     * @param taskId 태스크 id
     * @param requestDto 요청 dto
     * @return List<FindCommentResponseDto> 댓글 조회 응답 dto 리스트
     */
    @Transactional
    public List<FindCommentResponseDto> findByContents(
            Long taskId,
            FindCommentRequestDto requestDto){

        if(requestDto.getContents() == null || requestDto.getContents().isBlank()){
            throw new CustomException(COMMENT_NOT_ENTERED, COMMENT_NOT_ENTERED.getMessage());
        }

        List<Comment> commentList =
                commentRepository.findByTask_TaskIdAndContentsContainingAndDeletedFalse(
                        taskId, requestDto.getContents());

        if (commentList == null || commentList.isEmpty()) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }

        return commentList
                .stream()
                .map(FindCommentResponseDto::new)
                .collect(Collectors.toList());


    }




    /**
     * <p>댓글 수정</p>
     *
     * @param taskId 태스크 id
     * @param commentId 댓글 id
     * @param requestDto 요청 dto
     * @param userDto 로그인된 사용자 dto
     * @return UpdateCommentResponseDto 수정된 댓글 응답 dto
     */
    @Transactional
    public UpdateCommentResponseDto updateComment(
            Long taskId,
            Long commentId,
            UpdateCommentRequestDto requestDto,
            AuthUserDto userDto){


        User user = userRepository.findUserByEmailAndDeletedFalse(userDto.getEmail()).orElseThrow(() -> new CustomException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        Comment comment = commentRepository.findByCommentIdAndDeletedFalse(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));
        Task task = taskRepository.findTaskByTaskIdAndDeletedFalse(taskId).orElseThrow(() -> new CustomException(TASK_NOT_FOUND, TASK_NOT_FOUND.getMessage()));


        // 로그인된 사용자와 댓글 작성자의 이메일이 일치하지 않을 경우 예외 처리
        if(!comment.getAuthor().getEmail().equals(user.getEmail())){
            throw new CustomException(INVALID_REQUEST, INVALID_REQUEST.getMessage());
        }

        // 요청 dto에 댓글 내용이 입력되지 않을 경우 예외 처리
        if(requestDto.getContents() == null || requestDto.getContents().isBlank()){
            throw new CustomException(COMMENT_NOT_ENTERED, COMMENT_NOT_ENTERED.getMessage());
        }

        // 기존 댓글 내용과 수정 댓글 내용이 동일할 경우 예외 처리
        if(comment.getContents().equals(requestDto.getContents())){
            throw new CustomException(COMMENT_IS_EQUAL, COMMENT_IS_EQUAL.getMessage());
        }


        comment = new Comment(requestDto.getContents(), user, task);
        commentRepository.save(comment);

        return new UpdateCommentResponseDto(comment);


    }


    /**
     * <p>댓글 삭제</p>
     *
     * @param taskId 태스크 id
     * @param commentId 요청 dto
     * @param userDto 로그인된 사용자 dto
     * @return DeleteCommentResponseDto 삭제된 댓글 응답 dto
     */
    @Transactional
    public DeleteCommentResponseDto deleteComment(
            Long taskId,
            Long commentId,
            AuthUserDto userDto){


        User user = userRepository.findUserByEmailAndDeletedFalse(userDto.getEmail()).orElseThrow(() -> new CustomException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        Comment comment = commentRepository.findByCommentIdAndDeletedFalse(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));

        // 로그인된 사용자와 댓글 작성자의 이메일이 일치하지 않을 경우 예외 처리
        if(!comment.getAuthor().getEmail().equals(user.getEmail())){
            throw new CustomException(INVALID_REQUEST, INVALID_REQUEST.getMessage());
        }

        comment.delete();
        commentRepository.save(comment);


        return new DeleteCommentResponseDto(comment);

    }


}

package com.example.taskproject.comment.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.UserRole;
import com.example.taskproject.domain.comment.dto.*;
import com.example.taskproject.domain.comment.repository.CommentRepository;
import com.example.taskproject.domain.comment.service.CommentService;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 댓글 기능 테스트코드
 *
 * @author 이현하
 */
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성 테스트")
    void createCommentTest(){
        // given
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto("contents");
        AuthUserDto userDto = new AuthUserDto(1L, "l@ex.com", UserRole.USER);
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(1L, "title", user);
        Comment comment = new Comment("contents", user, task);

        given(userRepository.findUserByEmailAndDeletedFalse("l@ex.com")).willReturn(Optional.of(user));
        given(taskRepository.findTaskByTaskIdAndDeletedFalse(1L)).willReturn(Optional.of(task));
        given(commentRepository.save(any())).willReturn(comment);

        // when
        CreateCommentResponseDto responseDto = commentService.createComment(1L ,requestDto, userDto);

        // then
        assertNotNull(responseDto);
    }


    @Test
    @DisplayName("댓글 조회 테스트")
    void findCommentTest(){
        // given
        Long taskId = 1L;
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(taskId, "title", user);

        Comment comment1 = new Comment("댓글1", user, task);
        Comment comment2 = new Comment("댓글2", user, task);
        List<Comment> commentList = List.of(comment1, comment2);

        given(commentRepository.findByTask_TaskIdAndDeletedFalse(taskId)).willReturn(commentList);

        // when
        List<FindCommentResponseDto> responseDto = commentService.findAll(taskId);

        // then
        assertEquals(2, responseDto.size());
        assertEquals("댓글1", responseDto.get(0).getContents());
        assertEquals("댓글2", responseDto.get(1).getContents());
    }


    @Test
    @DisplayName("댓글 수정 테스트")
    void updateCommentTest(){
        // given
        Long taskId = 1L;
        Long commentId = 1L;
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(taskId, "title", user);
        UpdateCommentRequestDto requestDto = new UpdateCommentRequestDto("수정한 댓글");
        AuthUserDto userDto = new AuthUserDto(1L, "l@ex.com", UserRole.USER);

        Comment originComment = new Comment("원본 댓글", user, task);

        given(commentRepository.findByCommentIdAndDeletedFalse(commentId)).willReturn(Optional.of(originComment));
        given(taskRepository.findTaskByTaskIdAndDeletedFalse(taskId)).willReturn(Optional.of(task));
        given(userRepository.findUserByEmailAndDeletedFalse(userDto.getEmail())).willReturn(Optional.of(user));

        // when
        UpdateCommentResponseDto responseDto = commentService.updateComment(taskId, commentId, requestDto, userDto);

        // then
        assertNotNull(responseDto);
        assertEquals("수정한 댓글", responseDto.getContents());
    }


    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteCommentTest(){
        // given
        Long taskId = 1L;
        Long commentId = 1L;
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(taskId, "title", user);
        AuthUserDto userDto = new AuthUserDto(1L, "l@ex.com", UserRole.USER);

        Comment comment = new Comment("댓글1", user, task);

        given(userRepository.findUserByEmailAndDeletedFalse("l@ex.com")).willReturn(Optional.of(user));
        given(commentRepository.findByCommentIdAndDeletedFalse(commentId)).willReturn(Optional.of(comment));

        // when
        DeleteCommentResponseDto responseDto = commentService.deleteComment(taskId, commentId, userDto);

        // then
        assertNotNull(responseDto);
        assertTrue(responseDto.isDeleted());
    }
}

package com.example.taskproject.domain.comment.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.UserRole;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static com.example.taskproject.common.enums.CustomErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

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
    @Mock
    private ActiveLogService activeLogService;
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

        given(userRepository.findUserByEmailAndDeletedFalse("l@ex.com")).willReturn(Optional.of(user));
        given(taskRepository.findTaskByTaskIdAndDeletedFalse(1L)).willReturn(Optional.of(task));
        given(commentRepository.save(any())).willAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);

            Field field = Comment.class.getDeclaredField("commentId");
            field.setAccessible(true);
            field.set(comment, 1L);
            return comment;
        });

        // when
        CreateCommentResponseDto responseDto = commentService.createComment(1L ,requestDto, userDto);

        // then
        assertNotNull(responseDto);
        verify(activeLogService).logActivity(user.getUserId(), "COMMENT_CREATED", task.getTaskId());
    }

    @Test
    @DisplayName("내용 미입력으로 댓글 생성 실패")
    void EmptyContentsCommentTest(){
        // given
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto("");
        AuthUserDto userDto = new AuthUserDto(1L, "l@ex.com", UserRole.USER);
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(1L, "title", user);

        given(userRepository.findUserByEmailAndDeletedFalse("l@ex.com")).willReturn(Optional.of(user));
        given(taskRepository.findTaskByTaskIdAndDeletedFalse(1L)).willReturn(Optional.of(task));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                        commentService.createComment(1L, requestDto, userDto));

        assertEquals(COMMENT_NOT_ENTERED, exception.getErrorCode());
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
    @DisplayName("댓글 내용 검색 테스트")
    void findByContentsTest(){
        // given
        Long taskId = 1L;
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(taskId, "title", user);
        FindCommentRequestDto requestDto = new FindCommentRequestDto("댓글");

        Comment comment1 = new Comment("댓글1", user, task);
        Comment comment2 = new Comment("덧글2", user, task);
        List<Comment> commentList = List.of(comment1, comment2);

        given(commentRepository.findByTask_TaskIdAndContentsContainingAndDeletedFalse(taskId, "댓글"))
                .willReturn(commentList);

        // when
        List<FindCommentResponseDto> responseDto = commentService.findByContents(taskId, requestDto);

        // then
        assertEquals(2, responseDto.size());
        assertTrue(responseDto.get(0).getContents().contains("댓글"));
        assertFalse(responseDto.get(1).getContents().contains("댓글"));
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
        verify(activeLogService).logActivity(user.getUserId(), "COMMENT_UPDATED", originComment.getCommentId());
    }


    @Test
    @DisplayName("동일한 내용으로 댓글 수정 실패")
    void SameContentsFailUpdateComment(){
        // given
        Long taskId = 1L;
        Long commentId = 1L;
        String sameContent = "같은 댓글";
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(taskId, "title", user);

        Comment comment = new Comment(sameContent, user, task);
        UpdateCommentRequestDto requestDto = new UpdateCommentRequestDto(sameContent);
        AuthUserDto userDto = new AuthUserDto(1L, "l@ex.com", UserRole.USER);

        given(commentRepository.findByCommentIdAndDeletedFalse(commentId)).willReturn(Optional.of(comment));
        given(taskRepository.findTaskByTaskIdAndDeletedFalse(taskId)).willReturn(Optional.of(task));
        given(userRepository.findUserByEmailAndDeletedFalse(userDto.getEmail())).willReturn(Optional.of(user));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                commentService.updateComment(taskId, commentId, requestDto, userDto));

        assertEquals(COMMENT_IS_EQUAL, exception.getErrorCode());
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
        verify(activeLogService).logActivity(user.getUserId(), "COMMENT_DELETED", comment.getCommentId());
    }


    @Test
    @DisplayName("로그인한 사용자와 댓글 작성자가 동일하지 않을 때 댓글 삭제 실패 테스트")
    void NotSameUserEmailTest(){
        // given
        Long taskId = 1L;
        Long commentId = 1L;
        User user = new User(1L, "l@ex.com", "name");
        Task task = new Task(taskId, "title", user);
        Comment comment = new Comment("댓글1", user, task);

        AuthUserDto userDto = new AuthUserDto(1L, "fail@ex.com", UserRole.USER);

        given(commentRepository.findByCommentIdAndDeletedFalse(commentId)).willReturn(Optional.of(comment));
        given(userRepository.findUserByEmailAndDeletedFalse("fail@ex.com")).
                willReturn(Optional.of(new User(1L, "fail@ex.com", "other user")));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                commentService.deleteComment(taskId, commentId, userDto));

        assertEquals(EMAIL_NOT_SAME_COMMENT_AUTHOR, exception.getErrorCode());
    }
}

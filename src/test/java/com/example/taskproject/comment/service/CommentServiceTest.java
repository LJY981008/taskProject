package com.example.taskproject.comment.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.UserRole;
import com.example.taskproject.domain.comment.dto.CreateCommentRequestDto;
import com.example.taskproject.domain.comment.dto.CreateCommentResponseDto;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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

}

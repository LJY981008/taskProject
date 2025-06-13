package com.example.taskproject.domain.comment.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.exception.CustomException;
import com.example.taskproject.domain.comment.dto.CreateCommentRequestDto;
import com.example.taskproject.domain.comment.dto.CreateCommentResponseDto;
import com.example.taskproject.domain.comment.repository.CommentRepository;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.taskproject.common.enums.CustomErrorCode.TASK_NOT_FOUND;
import static com.example.taskproject.common.enums.CustomErrorCode.USER_NOT_FOUND;

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


    @Transactional
    public CreateCommentResponseDto createComment(Long taskId, CreateCommentRequestDto requestDto, AuthUserDto userDto){
        User user = userRepository.findUserByEmailAndDeletedFalse(userDto.getEmail()).orElseThrow(() -> new CustomException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        Task task = taskRepository.findTaskByTaskIdAndDeletedFalse(taskId).orElseThrow(() -> new CustomException(TASK_NOT_FOUND, TASK_NOT_FOUND.getMessage()));


        Comment comment = new Comment(requestDto.getContents(), user, task);
        commentRepository.save(comment);

        return new CreateCommentResponseDto(comment);
    }
}

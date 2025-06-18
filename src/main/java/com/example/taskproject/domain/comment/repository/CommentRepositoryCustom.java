package com.example.taskproject.domain.comment.repository;

import com.example.taskproject.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findByComments(Long taskId, Pageable pageable);

    Page<Comment> findByCommentsContent(Long taskId, String contents, Pageable pageable);
}

package com.example.taskproject.domain.comment.repository;

import com.example.taskproject.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCommentIdAndDeletedFalse(Long commentId);

    Page<Comment> findByTask_TaskIdAndDeletedFalse(Long taskId, Pageable pageable);

    Page<Comment> findByTask_TaskIdAndContentsContainingAndDeletedFalse(Long taskId, String contents, Pageable pageable);

}
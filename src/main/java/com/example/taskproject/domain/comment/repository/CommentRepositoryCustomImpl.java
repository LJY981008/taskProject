package com.example.taskproject.domain.comment.repository;

import com.example.taskproject.common.entity.Comment;
import com.example.taskproject.common.entity.QComment;
import com.example.taskproject.common.entity.QTask;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findByComments(Long taskId, Pageable pageable) {
        QComment comment = QComment.comment;

        List<Comment> content = queryFactory.selectFrom(comment)
                .join(comment.task, QTask.task).fetchJoin()
                .where(comment.task.taskId.eq(taskId)
                        .and(comment.deleted.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(comment.count())
                .from(comment)
                .where(comment.task.taskId.eq(taskId)
                        .and(comment.deleted.isFalse()))
                .fetchOne();

        return new PageImpl<>(content, pageable, count == null ? 0 : count);
    }

    @Override
    public Page<Comment> findByCommentsContent(Long taskId, String contents, Pageable pageable) {
        QComment comment = QComment.comment;

        List<Comment> content = queryFactory.selectFrom(comment)
                .join(comment.task, QTask.task).fetchJoin()
                .where(comment.task.taskId.eq(taskId)
                        .and(comment.deleted.isFalse())
                        .and(comment.contents.like("%" + contents + "%"))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(comment.count())
                .from(comment)
                .where(comment.task.taskId.eq(taskId)
                        .and(comment.deleted.isFalse()))
                .fetchOne();

        return new PageImpl<>(content, pageable, count == null ? 0 : count);
    }
}

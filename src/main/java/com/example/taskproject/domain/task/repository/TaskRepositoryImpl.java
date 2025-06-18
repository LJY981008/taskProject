package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.QTask;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.enums.TaskStatus;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Task> findByFilterTask(
            Pageable pageable,
            TaskStatus status,
            String search,
            Long assigneeId
    ) {
        QTask task = QTask.task;

        List<Task> tasks = queryFactory.selectFrom(task)
                .where(
                        task.taskStatus.eq(status),
                        task.title.like("%" + search + "%"),
                        task.manager.userId.eq(assigneeId),
                        task.deleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(task.count())
                .from(task)
                .where(
                        task.taskStatus.eq(status),
                        task.title.like("%" + search + "%"),
                        task.manager.userId.eq(assigneeId),
                        task.deleted.isFalse()
                )
                .fetchOne(); // 단일 결과를 가져옵니다.

        long actualTotalCount = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(tasks, pageable, actualTotalCount);
    }
}

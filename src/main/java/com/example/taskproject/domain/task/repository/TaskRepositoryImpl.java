package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.QTask;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.enums.TaskStatus;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Task> findByFilterTask(
            Pageable pageable,
            TaskStatus status
    ) {
        QTask task = QTask.task;

        return queryFactory.selectFrom(task)
                .where(task.taskStatus.eq(status))
                .where(task.deleted.isFalse())
                .limit(pageable.getPageSize())
                .offset(pageable.getPageNumber())
                .fetch();
    }
}

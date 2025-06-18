package com.example.taskproject.domain.statistics.repository;

import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.statistics.dto.*;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.taskproject.common.entity.QTask.task;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryImpl implements StatisticsRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public DashboardStats findDashboardStats(String authorUsername) {

        NumberExpression<Long> todoTasks = new CaseBuilder()
                .when(task.taskStatus.eq(TaskStatus.TODO)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> inProgressTasks = new CaseBuilder()
                .when(task.taskStatus.eq(TaskStatus.IN_PROGRESS)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> completedTasks = new CaseBuilder()
                .when(task.taskStatus.eq(TaskStatus.DONE)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> overDueTasks = new CaseBuilder()
                .when(
                        task.taskStatus.eq(TaskStatus.TODO)
                                .or(task.taskStatus.eq(TaskStatus.IN_PROGRESS))
                                .and(task.dueDate.lt(LocalDateTime.now()))
                )
                .then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> teamProgress = new CaseBuilder()
                .when(task.taskStatus.eq(TaskStatus.DONE)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> myTasksToday =  new CaseBuilder()
                .when(task.taskStatus.eq(TaskStatus.DONE)
                        .and(task.author.username.eq(authorUsername)))
                .then(1L)
                .otherwise(0L).sum();

        return queryFactory.select(
                        new QDashboardStats(
                                todoTasks,
                                inProgressTasks,
                                completedTasks,
                                task.count(),
                                overDueTasks,
                                teamProgress,
                                myTasksToday
                        )).from(task)
                .where(task.deleted.isFalse())
                .fetchOne();
    }
}
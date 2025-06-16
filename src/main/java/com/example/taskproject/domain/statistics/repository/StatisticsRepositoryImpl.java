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
    public List<StatusCount> findStatusCount() {
        return queryFactory.select(
                new QStatusCount(
                        task.taskStatus,
                        task.count()
                )).from(task)
                .where(task.deleted.isFalse())
                .groupBy(task.taskStatus)
                .fetch();
    }

    @Override
    public TeamTaskStatusCount findTeamTaskStatusCount(String authorUsername, TaskStatus taskStatus) {

        NumberExpression<Long> teamFinishTaskCount = new CaseBuilder()
                .when(task.taskStatus.eq(taskStatus)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> myFinishTaskCount = new CaseBuilder()
                .when(task.taskStatus.eq(taskStatus)
                        .and(task.author.username.eq(authorUsername)))
                .then(1L)
                .otherwise(0L).sum();


        return queryFactory.select(
                new QTeamTaskStatusCount(
                        task.count(),
                        teamFinishTaskCount,
                        myFinishTaskCount
                ))
                .from(task)
                .where(task.deleted.isFalse())
                .fetchOne();
    }

    @Override
    public WeekFinishTaskCount findWeekFinishTaskCounts(TaskStatus taskStatus, LocalDateTime start, LocalDateTime end) {

        NumberExpression<Long> weekTaskCount = new CaseBuilder()
                .when(task.createdAt.between(start, end))
                .then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> weekFinishTaskCount = new CaseBuilder()
                .when(task.createdAt.between(start, end)
                        .and(task.taskStatus.eq(taskStatus)))
                .then(1L)
                .otherwise(0L).sum();

        return queryFactory.select(
                new QWeekFinishTaskCount(
                        weekTaskCount,
                        weekFinishTaskCount
                ))
                .from(task)
                .where(task.deleted.isFalse())
                .fetchOne();
    }

    @Override
    public OverDueTaskCount findOverDueTaskCount() {
        NumberExpression<Long> overDueCount = new CaseBuilder()
                .when(
                        task.taskStatus.eq(TaskStatus.TODO)
                        .or(task.taskStatus.eq(TaskStatus.IN_PROGRESS))
                                .and(task.deadline.lt(LocalDateTime.now()))
                )
                .then(1L)
                .otherwise(0L).sum();

        return queryFactory.select(
                new QOverDueTaskCount(
                        overDueCount
                ))
                .from(task)
                .where(task.deleted.isFalse())
                .fetchOne();
    }
}

package com.example.taskproject.domain.statistics.repository;

import com.example.taskproject.common.dto.TaskResponseDto;
import com.example.taskproject.common.dto.UserInfo;
import com.example.taskproject.common.entity.QUser;
import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.statistics.dto.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.taskproject.common.entity.QComment.comment;
import static com.example.taskproject.common.entity.QTask.task;
import static com.example.taskproject.common.entity.QUser.user;

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

    @Override
    public MyTasksResponse findMyTasks(String authorUsername) {
        List<TaskResponseDto> todo = queryFactory.select(
                        Projections.constructor(
                                TaskResponseDto.class,
                                task.taskId,
                                task.title,
                                task.contents,
                                task.taskPriority,
                                task.taskStatus,
                                task.dueDate,
                                task.startedAt,
                                task.author.userId,
                                Projections.constructor(
                                        UserInfo.class,
                                        user.userId,
                                        user.username,
                                        user.name,
                                        user.email
                                ),
                                task.createdAt,
                                task.modifiedAt
                        ))
                        .from(task)
                        .join(task.author, user)
                        .where(task.taskStatus.eq(TaskStatus.TODO))
                        .where(task.author.email.eq(authorUsername))
                        .orderBy(task.taskPriority.desc())
                        .fetch();

        List<TaskResponseDto> inProgress = queryFactory.select(
                        Projections.constructor(
                                TaskResponseDto.class,
                                task.taskId,
                                task.title,
                                task.contents,
                                task.taskPriority,
                                task.taskStatus,
                                task.dueDate,
                                task.startedAt,
                                task.author.userId,
                                Projections.constructor(
                                        UserInfo.class,
                                        user.userId,
                                        user.username,
                                        user.name,
                                        user.email
                                ),
                                task.createdAt,
                                task.modifiedAt
                        ))
                        .from(task)
                        .join(task.author, user)
                        .where(task.taskStatus.eq(TaskStatus.IN_PROGRESS))
                        .where(task.author.email.eq(authorUsername))
                        .orderBy(task.taskPriority.desc())
                        .fetch();

        List<TaskResponseDto> done = queryFactory.select(
                        Projections.constructor(
                                TaskResponseDto.class,
                                task.taskId,
                                task.title,
                                task.contents,
                                task.taskPriority,
                                task.taskStatus,
                                task.dueDate,
                                task.startedAt,
                                task.author.userId,
                                Projections.constructor(
                                        UserInfo.class,
                                        user.userId,
                                        user.username,
                                        user.name,
                                        user.email
                                ),
                                task.createdAt,
                                task.modifiedAt
                        ))
                        .from(task)
                        .join(task.author, user)
                        .where(task.taskStatus.eq(TaskStatus.DONE))
                        .where(task.author.email.eq(authorUsername))
                        .orderBy(task.taskPriority.desc())
                        .fetch();

        return new MyTasksResponse(todo, inProgress, done);
    }
}
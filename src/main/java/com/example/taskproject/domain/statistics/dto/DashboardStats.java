package com.example.taskproject.domain.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;

public record DashboardStats(long todoTasks, long inProgressTasks, long completedTasks, long totalTasks,
                             long overdueTasks, long teamProgress, long myTasksToday) {
    @QueryProjection
    public DashboardStats {
    }
}

package com.example.taskproject.domain.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;

public record DashboardStats(Long todoTasks, Long inProgressTasks, Long completedTasks, Long totalTasks,
                             Long overdueTasks, Long teamProgress, Long myTasksToday) {
    @QueryProjection
    public DashboardStats {
    }
}

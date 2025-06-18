package com.example.taskproject.domain.statistics.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardStatsResponse {

    private long todoTasks;

    private long inProgressTasks;

    private long completedTasks;

    private long totalTasks;

    private long overdueTasks;

    private long teamProgress;

    private long myTasksToday;

    private long completionRate;
}

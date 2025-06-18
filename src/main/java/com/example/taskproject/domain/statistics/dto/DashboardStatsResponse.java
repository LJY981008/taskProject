package com.example.taskproject.domain.statistics.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardStatsResponse {

    private Long todoTasks;

    private Long inProgressTasks;

    private Long completedTasks;

    private Long totalTasks;

    private Long overdueTasks;

    private Long teamProgress;

    private Long myTasksToday;

    private Long completionRate;
}

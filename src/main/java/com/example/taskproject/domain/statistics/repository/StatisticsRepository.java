package com.example.taskproject.domain.statistics.repository;

import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.statistics.dto.StatusCount;
import com.example.taskproject.domain.statistics.dto.TeamTaskStatusCount;
import com.example.taskproject.domain.statistics.dto.WeekFinishTaskCount;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository {

    List<StatusCount> findStatusCount();

    TeamTaskStatusCount findTeamTaskStatusCount(String authorUsername, TaskStatus taskStatus);

    WeekFinishTaskCount countWeekFinishTaskCountJPQL(TaskStatus taskStatus, LocalDateTime start, LocalDateTime end);
}

package com.example.taskproject.domain.statistics.dto;

import com.example.taskproject.common.dto.TaskResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyTasksResponse {
    private  List<TaskResponseDto> todayTasks;
    private  List<TaskResponseDto> upcomingTasks;
    private  List<TaskResponseDto> overdueTasks;

    public MyTasksResponse(List<TaskResponseDto> todayTasks, List<TaskResponseDto> upcomingTasks, List<TaskResponseDto> overdueTasks) {
        this.todayTasks = todayTasks;
        this.upcomingTasks = upcomingTasks;
        this.overdueTasks = overdueTasks;
    }
}

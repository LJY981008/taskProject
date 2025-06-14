package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.common.util.Responser;
import com.example.taskproject.domain.statistics.dto.GetTaskStatusResponse;
import com.example.taskproject.domain.statistics.dto.GetTeamFinishTaskResponse;
import com.example.taskproject.domain.statistics.dto.GetWeekFinishTaskResponse;
import com.example.taskproject.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TaskRepository taskRepository;

    public GetTaskStatusResponse getTaskStatusCounts() {

        List<TaskRepository.StatusCount> taskCounts = taskRepository.findStatusCountJPQL();

        long todo = 0L;
        long inProgress = 0L;
        long done = 0L;

        for (TaskRepository.StatusCount count : taskCounts) {
            switch (count.getTaskStatus()) {
                case TODO -> todo = count.getCount();
                case IN_PROGRESS -> inProgress = count.getCount();
                case DONE -> done = count.getCount();
            }
        }

        return new GetTaskStatusResponse(todo, inProgress, done);
    }

    public GetTeamFinishTaskResponse getTeamFinishTaskCounts(String email) {

        TaskRepository.TeamTaskStatusCount teamTaskStatusCount = taskRepository.countTeamTaskStatusCountJPQL(email, TaskStatus.DONE);

        return new GetTeamFinishTaskResponse(
                teamTaskStatusCount.getTotalTaskCount(),
                teamTaskStatusCount.getTeamFinishTaskCount(),
                teamTaskStatusCount.getMyFinishTaskCount());
    }

    public GetWeekFinishTaskResponse getWeekFinishTaskCounts(LocalDate from) {
        LocalDateTime end = from.atStartOfDay();
        LocalDateTime start = end.minusDays(7);

        TaskRepository.WeekFinishTaskCount weekFinishTaskCount = taskRepository.countWeekFinishTaskCountJPQL(TaskStatus.DONE, start, end);

        return new GetWeekFinishTaskResponse(
                weekFinishTaskCount.getWeekTaskCount(),
                weekFinishTaskCount.getWeekFinishTaskCount());
    }
}
package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.statistics.dto.*;
import com.example.taskproject.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TaskRepository taskRepository;

    public GetTaskStatusResponse getTaskStatusCounts() {

        List<StatusCount> taskCounts = taskRepository.findStatusCount();

        long todo = 0L;
        long inProgress = 0L;
        long done = 0L;

        for (StatusCount count : taskCounts) {
            switch (count.getTaskStatus()) {
                case TODO -> todo = count.getCount();
                case IN_PROGRESS -> inProgress = count.getCount();
                case DONE -> done = count.getCount();
            }
        }

        return new GetTaskStatusResponse(todo, inProgress, done);
    }

    public TeamTaskStatusCount getTeamFinishTaskCounts(String email) {
        return taskRepository.findTeamTaskStatusCount(email, TaskStatus.DONE);
    }

    public WeekFinishTaskCount getWeekFinishTaskCounts(LocalDate from) {
        LocalDateTime end = from.atTime(23, 59, 59, 999_999_999);
        LocalDateTime start = end.minusDays(7);

        return taskRepository.findWeekFinishTaskCounts(TaskStatus.DONE, start, end);
    }

    public OverDueTaskCount getOverdueTaskCounts() {
        return taskRepository.findOverDueTaskCount();
    }
}
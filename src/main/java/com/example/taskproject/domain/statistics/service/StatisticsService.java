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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TaskRepository taskRepository;

    public ResponseEntity<Map<String, Object>> getTaskStatusCounts() {

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

        GetTaskStatusResponse getTaskStatusResponse = new GetTaskStatusResponse(todo, inProgress, done);
        return Responser.responseEntity(getTaskStatusResponse, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> getTeamFinishTaskCounts(User user) {

        Long totalTaskCount = taskRepository.countAllByDeletedFalse();
        Long teamFinishTaskCount = taskRepository.countByDeletedFalseAndTaskStatus(TaskStatus.DONE);
        Long myFinishTaskCount = taskRepository.countByDeletedFalseAndAuthor_UsernameAndTaskStatus(user.getUsername(), TaskStatus.DONE);

        GetTeamFinishTaskResponse getTeamFinishTaskResponse = new GetTeamFinishTaskResponse(totalTaskCount, teamFinishTaskCount, myFinishTaskCount);
        return Responser.responseEntity(getTeamFinishTaskResponse, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> getWeekFinishTaskCounts() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        Long weekTaskCount = taskRepository.countByDeletedFalseAndCreatedAtBetween(sevenDaysAgo, now);
        Long weekFinishTaskCount = taskRepository.countByDeletedFalseAndTaskStatusAndCreatedAtBetween(TaskStatus.DONE, sevenDaysAgo, now);

        GetWeekFinishTaskResponse getWeekFinishTaskResponse = new GetWeekFinishTaskResponse(weekTaskCount, weekFinishTaskCount);
        return Responser.responseEntity(getWeekFinishTaskResponse, HttpStatus.OK);
    }
}
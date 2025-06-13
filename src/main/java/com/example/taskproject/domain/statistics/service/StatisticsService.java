package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.util.Responser;
import com.example.taskproject.domain.statistics.dto.GetTaskStatusResponse;
import com.example.taskproject.domain.statistics.dto.GetTeamFinishTaskResponse;
import com.example.taskproject.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private TaskRepository taskRepository;

    public ResponseEntity<Map<String, Object>> getTaskStatusCounts(User user) {

        GetTaskStatusResponse getTaskStatusResponse = new GetTaskStatusResponse(0L,0L,0L);
        return Responser.responseEntity(getTaskStatusResponse, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> getTeamFinishTaskCounts(User user) {

        GetTeamFinishTaskResponse getTeamFinishTaskResponse = new GetTeamFinishTaskResponse(0L, 0L, 0L);
        return Responser.responseEntity(getTeamFinishTaskResponse, HttpStatus.OK);
    }
}
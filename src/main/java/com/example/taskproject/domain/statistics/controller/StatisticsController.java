package com.example.taskproject.domain.statistics.controller;

import com.example.taskproject.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private StatisticsService statisticsService;

    @GetMapping("/task-status")
    public ResponseEntity<Map<String, Object>> getTaskStatusStatistics(
            @AuthenticationPrincipal User user
    ) {
        return statisticsService.getTaskStatusCounts(user);
    }

    @GetMapping("/team-progress")
    public ResponseEntity<Map<String, Object>> getTeamProgressStatistics(
            @AuthenticationPrincipal User user
    ) {
        return statisticsService.getTeamFinishTaskCounts(user);
    }

    @GetMapping("/weekly-trend")
    public ResponseEntity<Map<String, Object>> getWeekFinishTaskStatistics(
            @AuthenticationPrincipal User user
    ) {
        return statisticsService.getWeekFinisshTaskCounts(user);
    }
}
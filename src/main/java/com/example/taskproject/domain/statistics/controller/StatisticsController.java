package com.example.taskproject.domain.statistics.controller;

import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/task-status")
    public ResponseEntity<Map<String, Object>> getTaskStatusStatistics(
    ) {
        return CustomMapper.responseEntity(statisticsService.getTaskStatusCounts(), HttpStatus.OK, true);
    }

    @GetMapping("/team-progress")
    public ResponseEntity<Map<String, Object>> getTeamProgressStatistics(
            @AuthenticationPrincipal User user
    ) {
        return CustomMapper.responseEntity(statisticsService.getTeamFinishTaskCounts(user.getUsername()), HttpStatus.OK, true);
    }

    @GetMapping("/weekly-trend")
    public ResponseEntity<Map<String, Object>> getWeekFinishTaskStatistics(
            @RequestParam(required = false) LocalDate from
    ) {
        return CustomMapper.responseEntity(statisticsService.getWeekFinishTaskCounts(from), HttpStatus.OK, true);
    }

    @GetMapping("/over-due")
    public ResponseEntity<Map<String, Object>> getOverdueTaskStatistics() {
        return CustomMapper.responseEntity(statisticsService.getOverdueTaskCounts(),HttpStatus.OK, true);
    }
}
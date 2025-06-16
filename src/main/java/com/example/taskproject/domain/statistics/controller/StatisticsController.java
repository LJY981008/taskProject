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

    /**
     * 전체 작업 상태별 통계를 조회합니다.
     *
     * @return 할 일, 진행 중, 완료 상태의 작업 개수를 포함한 응답
     */
    @GetMapping("/task-status")
    public ResponseEntity<Map<String, Object>> getTaskStatusStatistics(
    ) {
        return CustomMapper.responseEntity(statisticsService.getTaskStatusCounts(), HttpStatus.OK, true);
    }

    /**
     * 현재 로그인한 사용자가 속한 팀의 완료된 작업 수를 조회합니다.
     *
     * @param user 현재 인증된 사용자
     * @return 팀의 완료된 작업 개수를 포함한 응답
     */
    @GetMapping("/team-progress")
    public ResponseEntity<Map<String, Object>> getTeamProgressStatistics(
            @AuthenticationPrincipal User user
    ) {
        return CustomMapper.responseEntity(statisticsService.getTeamFinishTaskCounts(user.getUsername()), HttpStatus.OK, true);
    }

    /**
     * 주어진 날짜를 기준으로 최근 7일 간의 완료된 작업 수를 조회합니다.
     * <p>
     * 파라미터를 생략하면 null이 전달되므로 서비스 로직에서 null 처리 필요.
     *
     * @param from 기준 날짜 (Optional). 이 날짜의 23:59:59.999까지 포함됩니다.
     * @return 주간 완료 작업 수를 포함한 응답
     */
    @GetMapping("/weekly-trend")
    public ResponseEntity<Map<String, Object>> getWeekFinishTaskStatistics(
            @RequestParam(required = false) LocalDate from
    ) {
        return CustomMapper.responseEntity(statisticsService.getWeekFinishTaskCounts(from), HttpStatus.OK, true);
    }

    /**
     * 마감 기한이 초과된 작업의 개수를 조회합니다.
     *
     * @return 마감 기한 초과 작업 수를 포함한 응답
     */
    @GetMapping("/over-due")
    public ResponseEntity<Map<String, Object>> getOverdueTaskStatistics() {
        return CustomMapper.responseEntity(statisticsService.getOverdueTaskCounts(),HttpStatus.OK, true);
    }
}
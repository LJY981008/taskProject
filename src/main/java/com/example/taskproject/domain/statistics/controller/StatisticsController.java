package com.example.taskproject.domain.statistics.controller;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.statistics.dto.*;
import com.example.taskproject.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 대시보드 통계 정보를 조회합니다.
     *
     * <p>인증된 사용자의 정보를 기반으로 통계 데이터를 조회한 뒤,
     * {@link DashboardStatsResponse} 형태로 응답을 반환합니다.
     * 응답은 커스텀 유틸리티 {@code CustomMapper.responseToMap(...)}를 통해 Map 형식으로 변환되어 반환됩니다.</p>
     *
     * @author kimyongjun0129
     * @param authUser 현재 인증된 사용자 정보 (스프링 시큐리티에서 주입)
     * @return 대시보드 통계 정보를 포함한 HTTP 200 OK 응답
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(
            @AuthenticationPrincipal AuthUserDto authUser
    ) {
        DashboardStatsResponse taskStatusCounts = statisticsService.getDashboardStatsCount(authUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(taskStatusCounts, true));
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<Map<String, Object>> getMyTasks(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        MyTasksResponse myTasksListResponse = statisticsService.getMyTasks(authUserDto);
        System.out.println("myTasksListResponse = " + myTasksListResponse);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(myTasksListResponse, true));
    }
}
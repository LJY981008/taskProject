package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.domain.statistics.dto.*;
import com.example.taskproject.domain.task.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TaskRepository taskRepository;

    /**
     * 사용자의 대시보드 통계 정보를 조회합니다.
     *
     * <p>이 메서드는 로그인된 사용자의 이메일을 기준으로 {@link DashboardStats} 정보를 조회하고,
     * 각 항목을 {@link DashboardStatsResponse} 객체로 매핑하여 반환합니다.
     * 완료율(completionRate)은 완료된 작업 수를 전체 작업 수로 나눈 후 백분율로 계산하며,
     * 전체 작업 수가 0인 경우 0%로 처리됩니다.</p>
     *
     * @author kimyongjun0129
     * @param authUser 인증된 사용자 정보 (이메일 포함)
     * @return {@link DashboardStatsResponse} 객체 - 대시보드 통계 응답
     */
    public DashboardStatsResponse getDashboardStatsCount(AuthUserDto authUser) {

        String authEmail = authUser.getEmail();

        DashboardStats dashboardStats = taskRepository.findDashboardStats(authEmail);


        long completionRate = dashboardStats.totalTasks() == 0 ? 0
                : Math.round(
                        (double) dashboardStats.completedTasks() / dashboardStats.totalTasks() * 100
        );

        return DashboardStatsResponse.builder()
                .todoTasks(dashboardStats.todoTasks())
                .inProgressTasks(dashboardStats.inProgressTasks())
                .completedTasks(dashboardStats.completedTasks())
                .overdueTasks(dashboardStats.overdueTasks())
                .totalTasks(dashboardStats.totalTasks())
                .teamProgress(dashboardStats.teamProgress())
                .myTasksToday(dashboardStats.myTasksToday())
                .completionRate(completionRate)
                .build();
    }

    @Transactional
    public MyTasksResponse getMyTasks(AuthUserDto authUser) {

        String authEmail = authUser.getEmail();
        return taskRepository.findMyTasks(authEmail);
    }
}
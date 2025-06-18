package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.dto.AuthUserDto;
import com.example.taskproject.common.enums.UserRole;
import com.example.taskproject.domain.statistics.dto.*;
import com.example.taskproject.domain.task.repository.TaskRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    void 상태별_Task_수_가져오기_성공() {
        //given
        AuthUserDto authUser = new AuthUserDto(1L, "nana@naver.com", UserRole.USER);
        DashboardStats dashboardStats = new DashboardStats(10L,10L,10L,30L, 10L,10L,3L);
        Long completionRate = dashboardStats.totalTasks().equals(0L) ? 0L
                : Math.round(
                (double) dashboardStats.completedTasks() / dashboardStats.totalTasks() * 100
        );

        given(taskRepository.findDashboardStats(any(String.class))).willReturn(dashboardStats);

        //when
        DashboardStatsResponse dashboardStatsCount = statisticsService.getDashboardStatsCount(authUser);

        //then
        assertEquals(dashboardStats.todoTasks(), dashboardStatsCount.getTodoTasks());
        assertEquals(dashboardStats.inProgressTasks(), dashboardStatsCount.getInProgressTasks());
        assertEquals(dashboardStats.completedTasks(), dashboardStatsCount.getCompletedTasks());
        assertEquals(dashboardStats.totalTasks(), dashboardStatsCount.getTotalTasks());
        assertEquals(dashboardStats.teamProgress(), dashboardStatsCount.getTeamProgress());
        assertEquals(dashboardStats.myTasksToday(), dashboardStatsCount.getMyTasksToday());
        assertEquals(dashboardStats.overdueTasks(), dashboardStatsCount.getOverdueTasks());
        assertEquals(completionRate, dashboardStatsCount.getCompletionRate());
    }
}
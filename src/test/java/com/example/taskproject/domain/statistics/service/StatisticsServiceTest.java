package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.statistics.dto.*;
import com.example.taskproject.domain.task.repository.TaskRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        List<StatusCount> statusCounts = List.of(
                new StatusCount(TaskStatus.TODO, 2L),
                new StatusCount(TaskStatus.IN_PROGRESS, 3L)
        );

        GetTaskStatusResponse getTaskStatusResponse = new GetTaskStatusResponse(2L, 3L, 0L);

        given(taskRepository.findStatusCount()).willReturn(statusCounts);

        //when
        GetTaskStatusResponse taskStatusCounts = statisticsService.getTaskStatusCounts();

        //then
        assertEquals(taskStatusCounts.getToDoCount(), getTaskStatusResponse.getToDoCount());
        assertEquals(taskStatusCounts.getInProgressCount(), getTaskStatusResponse.getInProgressCount());
        assertEquals(taskStatusCounts.getDoneCount(), getTaskStatusResponse.getDoneCount());
    }

    @Test
    void 팀_완료_Task_수_가져오기_성공() {
        //given
        TeamTaskStatusCount teamTaskStatusCount = new TeamTaskStatusCount(30L,15L,6L);
        GetTeamFinishTaskResponse getTeamFinishTaskResponse = new GetTeamFinishTaskResponse(30L, 15L, 6L);

        given(taskRepository.findTeamTaskStatusCount("nana@naver.com", TaskStatus.DONE)).willReturn(teamTaskStatusCount);

        //when
        GetTeamFinishTaskResponse teamFinishTaskCounts = statisticsService.getTeamFinishTaskCounts("nana@naver.com");

        //then
        assertEquals(teamFinishTaskCounts.getTotalTaskCount(), getTeamFinishTaskResponse.getTotalTaskCount());
        assertEquals(teamFinishTaskCounts.getTeamFinishTaskCount(), getTeamFinishTaskResponse.getTeamFinishTaskCount());
        assertEquals(teamFinishTaskCounts.getMyFinishTaskCount(), getTeamFinishTaskResponse.getMyFinishTaskCount());
    }

    @Test
    void 주간_팀_Task_완료_수_가져오기_성공() {
        //given
        WeekFinishTaskCount weekFinishTaskCount = new WeekFinishTaskCount(100L, 80L);
        LocalDate from = LocalDate.now();
        LocalDateTime end = from.atStartOfDay();
        LocalDateTime start = end.minusDays(7);

        GetWeekFinishTaskResponse getWeekFinishTaskResponse = new GetWeekFinishTaskResponse(100L, 80L);

        given(taskRepository.countWeekFinishTaskCountJPQL(TaskStatus.DONE, start, end)).willReturn(weekFinishTaskCount);

        //when
        GetWeekFinishTaskResponse weekFinishTaskCounts = statisticsService.getWeekFinishTaskCounts(from);

        //then
        assertEquals(weekFinishTaskCounts.getWeekTaskCount(), getWeekFinishTaskResponse.getWeekTaskCount());
        assertEquals(weekFinishTaskCounts.getWeekFinishTaskCount(), getWeekFinishTaskResponse.getWeekFinishTaskCount());
    }
}
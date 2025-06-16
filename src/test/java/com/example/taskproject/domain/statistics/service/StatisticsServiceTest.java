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
                new StatusCount(TaskStatus.IN_PROGRESS, 3L),
                new StatusCount(TaskStatus.DONE, 0L)
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

        given(taskRepository.findTeamTaskStatusCount("nana@naver.com", TaskStatus.DONE)).willReturn(teamTaskStatusCount);

        //when
        TeamTaskStatusCount teamFinishTaskCounts = statisticsService.getTeamFinishTaskCounts("nana@naver.com");

        //then
        assertEquals(30L, teamFinishTaskCounts.getTotalTaskCount());
        assertEquals(15L, teamFinishTaskCounts.getTeamFinishTaskCount());
        assertEquals(6L, teamFinishTaskCounts.getMyFinishTaskCount());
    }

    @Test
    void 주간_팀_Task_완료_수_가져오기_성공() {
        //given
        WeekFinishTaskCount weekFinishTaskCount = new WeekFinishTaskCount(100L, 80L);
        LocalDate from = LocalDate.now();
        LocalDateTime end = from.atTime(23, 59, 59, 999_999_999);
        LocalDateTime start = end.minusDays(7);

        given(taskRepository.findWeekFinishTaskCounts(TaskStatus.DONE, start, end)).willReturn(weekFinishTaskCount);

        //when
        WeekFinishTaskCount weekFinishTaskCounts = statisticsService.getWeekFinishTaskCounts(from);

        //then
        assertEquals(100L, weekFinishTaskCounts.getWeekTaskCount());
        assertEquals(80L, weekFinishTaskCounts.getWeekFinishTaskCount());
    }

    @Test
    void TODO_또는_IN_PROGRESS_중_마감된_Task_수_가져오기_성공() {
        //given
        OverDueTaskCount overDueTaskCount = new OverDueTaskCount(40L);

        given(taskRepository.findOverDueTaskCount()).willReturn(overDueTaskCount);

        //when
        OverDueTaskCount overdueTaskCounts = statisticsService.getOverdueTaskCounts();

        //then
        assertEquals(40L, overdueTaskCounts.getOverDusTaskCount());
    }
}
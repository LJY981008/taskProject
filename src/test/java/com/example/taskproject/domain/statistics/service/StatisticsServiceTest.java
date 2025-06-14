package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.statistics.dto.GetTaskStatusResponse;
import com.example.taskproject.domain.statistics.dto.GetTeamFinishTaskResponse;
import com.example.taskproject.domain.task.repository.TaskRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    void 상태별_Task_수_가져오기_성공() {
        //given
        List<TaskRepository.StatusCount> statusCounts = List.of(
                new TaskRepository.StatusCount() {
                    @Override
                    public TaskStatus getTaskStatus() {
                        return TaskStatus.TODO;
                    }

                    @Override
                    public Long getCount() {
                        return 2L;
                    }
                },
                new TaskRepository.StatusCount() {
                    @Override
                    public TaskStatus getTaskStatus() {
                        return TaskStatus.IN_PROGRESS;
                    }

                    @Override
                    public Long getCount() {
                        return 3L;
                    }
                }
        );

        GetTaskStatusResponse getTaskStatusResponse = new GetTaskStatusResponse(2L, 3L, 0L);

        given(taskRepository.findStatusCountJPQL()).willReturn(statusCounts);

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
        TaskRepository.TeamTaskStatusCount teamTaskStatusCount = new TaskRepository.TeamTaskStatusCount() {
            @Override
            public Long getTotalTaskCount() {
                return 30L;
            }

            @Override
            public Long getTeamFinishTaskCount() {
                return 15L;
            }

            @Override
            public Long getMyFinishTaskCount() {
                return 6L;
            }
        };

        GetTeamFinishTaskResponse getTeamFinishTaskResponse = new GetTeamFinishTaskResponse(30L, 15L, 6L);

        given(taskRepository.countTeamTaskStatusCountJPQL("nana@naver.com", TaskStatus.DONE)).willReturn(teamTaskStatusCount);

        //when
        GetTeamFinishTaskResponse teamFinishTaskCounts = statisticsService.getTeamFinishTaskCounts("nana@naver.com");

        //then
        assertEquals(teamFinishTaskCounts.getTotalTaskCount(), getTeamFinishTaskResponse.getTotalTaskCount());
        assertEquals(teamFinishTaskCounts.getTeamFinishTaskCount(), getTeamFinishTaskResponse.getTeamFinishTaskCount());
        assertEquals(teamFinishTaskCounts.getMyFinishTaskCount(), getTeamFinishTaskResponse.getMyFinishTaskCount());
    }

    @Test
    void getWeekFinishTaskCounts() {
    }
}
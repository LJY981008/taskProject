package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.statistics.dto.*;
import com.example.taskproject.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TaskRepository taskRepository;

    /**
     * 전체 작업의 상태별 개수를 조회합니다.
     *
     * @return 할 일(TODO), 진행 중(IN_PROGRESS), 완료(DONE) 상태의 작업 개수를 담은 응답 객체입니다. {@link GetTaskStatusResponse}
     */
    public GetTaskStatusResponse getTaskStatusCounts() {

        List<StatusCount> taskCounts = taskRepository.findStatusCount();

        long todo = 0L;
        long inProgress = 0L;
        long done = 0L;

        for (StatusCount count : taskCounts) {
            switch (count.getTaskStatus()) {
                case TODO -> todo = count.getCount();
                case IN_PROGRESS -> inProgress = count.getCount();
                case DONE -> done = count.getCount();
            }
        }

        return new GetTaskStatusResponse(todo, inProgress, done);
    }

    /**
     * 특정 사용자가 속한 팀의 완료된 작업 개수를 조회합니다.
     *
     * @param email 사용자 이메일 (팀 식별용)
     * @return 팀 전체의 완료된 작업 개수를 담은 DTO {@link TeamTaskStatusCount}
     */
    public TeamTaskStatusCount getTeamFinishTaskCounts(String email) {
        return taskRepository.findTeamTaskStatusCount(email, TaskStatus.DONE);
    }

    /**
     * 특정 날짜를 기준으로 7일 전부터 해당 날짜까지의 완료된 작업 개수를 조회합니다.
     * <p>
     * 기준 날짜는 해당 날짜의 23:59:59.999 까지 포함됩니다.
     *
     * @param from 기준 날짜 (포함)
     * @return 주간 완료된 작업 개수를 담은 DTO {@link WeekFinishTaskCount}
     */
    public WeekFinishTaskCount getWeekFinishTaskCounts(LocalDate from) {
        LocalDateTime end = from.atTime(23, 59, 59, 999_999_999);
        LocalDateTime start = end.minusDays(7);

        return taskRepository.findWeekFinishTaskCounts(TaskStatus.DONE, start, end);
    }

    /**
     * 마감 기한이 지난 작업 개수를 조회합니다.
     *
     * @return 마감 기한 초과 작업 수를 담은 DTO {@link OverDueTaskCount}
     */
    public OverDueTaskCount getOverdueTaskCounts() {
        return taskRepository.findOverDueTaskCount();
    }
}
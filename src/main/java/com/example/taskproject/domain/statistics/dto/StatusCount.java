package com.example.taskproject.domain.statistics.dto;

import com.example.taskproject.common.enums.TaskStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class StatusCount {

    private final TaskStatus taskStatus;
    private final long count;

    @QueryProjection
    public StatusCount(TaskStatus taskStatus, long count) {
        this.taskStatus = taskStatus;
        this.count = count;
    }
}

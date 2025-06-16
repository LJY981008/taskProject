package com.example.taskproject.domain.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class WeekFinishTaskCount {

    private final long weekTaskCount;
    private final long weekFinishTaskCount;

    @QueryProjection
    public WeekFinishTaskCount(long weekTaskCount, long weekFinishTaskCount) {
        this.weekTaskCount = weekTaskCount;
        this.weekFinishTaskCount = weekFinishTaskCount;
    }
}

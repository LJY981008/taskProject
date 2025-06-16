package com.example.taskproject.domain.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OverDueTaskCount {
    private final long overDusTaskCount;

    @QueryProjection
    public OverDueTaskCount(long overDusTaskCount) {
        this.overDusTaskCount = overDusTaskCount;
    }
}

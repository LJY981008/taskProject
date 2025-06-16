package com.example.taskproject.domain.statistics.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class TeamTaskStatusCount {
    private final long totalTaskCount;
    private final long teamFinishTaskCount;
    private final long myFinishTaskCount;

    @QueryProjection
    public TeamTaskStatusCount(long totalTaskCount, long teamFinishTaskCount, long myFinishTaskCount) {
        this.totalTaskCount = totalTaskCount;
        this.teamFinishTaskCount = teamFinishTaskCount;
        this.myFinishTaskCount = myFinishTaskCount;
    }
}

package com.example.taskproject.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetTeamFinishTaskResponse {

    private Long totalTaskCount;

    private Long teamFinishTaskCount;

    private Long myFinishTaskCount;
}

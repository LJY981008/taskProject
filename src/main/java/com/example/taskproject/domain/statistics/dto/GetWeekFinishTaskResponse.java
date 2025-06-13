package com.example.taskproject.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetWeekFinishTaskResponse {

    private Long weekTaskCount;

    private Long weekFinishTaskCount;
}

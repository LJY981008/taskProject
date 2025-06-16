package com.example.taskproject.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetTaskStatusResponse {

    private Long toDoCount;

    private Long inProgressCount;

    private Long doneCount;
}

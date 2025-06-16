package com.example.taskproject.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetOverDueTaskResponse {

    private final Long overDueCount;
}

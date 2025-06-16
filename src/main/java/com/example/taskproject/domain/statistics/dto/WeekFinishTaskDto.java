package com.example.taskproject.domain.statistics.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WeekFinishTaskDto {

    private final LocalDate date = LocalDate.now();
}

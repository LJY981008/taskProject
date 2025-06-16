package com.example.taskproject.common.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Getter
//@AllArgsConstructor
@NoArgsConstructor
public class LogRequestDto {
    private Long userId;
    private String activityType;
    private Long targetId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int page = 0;
    private int size = 10;
    private boolean sortByTime = true;

    public Pageable toPageable(){
        if(sortByTime) return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        else return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "activityType"));
    }

    public LogRequestDto(Long userId, String activityType, Long targetId, LocalDateTime startDate, LocalDateTime endDate, int page, int size, boolean sortByTime){
        this.userId = userId;
        this.activityType = activityType;
        this.targetId = targetId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.page = page;
        this.size = size;
        this.sortByTime = sortByTime;
    }
}

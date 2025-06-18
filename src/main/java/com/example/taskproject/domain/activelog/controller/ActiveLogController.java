package com.example.taskproject.domain.activelog.controller;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActiveLogController {
    private final ActiveLogService activeLogService;

    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> findLogsByConditions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) Long targetId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean sortByTime
    ){
        LogRequestDto logRequest = new LogRequestDto(
                userId,
                activityType,
                targetId,
                startDate,
                endDate,
                page,
                size,
                sortByTime
        );

        Pageable pageable = logRequest.toPageable();
        Page<ActiveLog> logPage = activeLogService.getActiveLogsByConditions(logRequest, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(logPage, true));
    }

}

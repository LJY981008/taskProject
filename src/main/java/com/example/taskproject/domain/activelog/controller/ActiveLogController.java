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

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActiveLogController {
    private final ActiveLogService activeLogService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findLogsByConditions(
            @Valid @RequestBody LogRequestDto logRequest
    ){
        Pageable pageable = logRequest.toPageable();
        Page<ActiveLog> logPage = activeLogService.getActiveLogsByConditions(logRequest, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomMapper.responseToMap(logPage, true));
    }

}

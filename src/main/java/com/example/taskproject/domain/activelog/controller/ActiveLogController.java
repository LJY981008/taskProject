package com.example.taskproject.domain.activelog.controller;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.common.util.CustomMapper;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActiveLogController {
    private final ActiveLogService activeLogService;

    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> findLogsByConditions(@ModelAttribute LogRequestDto logRequest){
        Page<ActiveLog> logPage = activeLogService.getActiveLogsByConditions(logRequest, logRequest.toPageable());

        return ResponseEntity.ok(CustomMapper.responseToMap(logPage, true));
    }
}

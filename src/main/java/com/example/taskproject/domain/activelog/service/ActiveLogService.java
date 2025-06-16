package com.example.taskproject.domain.activelog.service;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.domain.activelog.repository.ActiveLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActiveLogService {
    private final ActiveLogRepository activeLogRepository;

    public Page<ActiveLog> getActiveLogsByConditions(LogRequestDto logRequest, Pageable pageable){
        return activeLogRepository.findActiveLogsDynamic(logRequest, pageable);
    }
}

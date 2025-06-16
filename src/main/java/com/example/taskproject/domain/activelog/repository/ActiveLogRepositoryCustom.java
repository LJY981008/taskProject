package com.example.taskproject.domain.activelog.repository;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActiveLogRepositoryCustom {
    Page<ActiveLog> findActiveLogsDynamic(LogRequestDto logRequest, Pageable pageable);
}

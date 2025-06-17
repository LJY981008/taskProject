package com.example.taskproject.domain.activelog.service;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.domain.activelog.repository.ActiveLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class ActiveLogService {
    private final ActiveLogRepository activeLogRepository;

    public Page<ActiveLog> getActiveLogsByConditions(LogRequestDto logRequest, Pageable pageable){
        return activeLogRepository.findActiveLogsDynamic(logRequest, pageable);
    }

    /**
     * @author 김도연
     * @param userId 사용자(주체) userID값
     * @param activityType 활동 내역(LOGIN, TASK_UPDATE 등)
     * @param targetId 대상 ID값
     * request HttpServletRequest, Ip, Method, URL값 저장 위해 사용
     * use example : activeLogService.logActivity(1L, "LOGIN", 2L, request);
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logActivity(Long userId, String activityType, Long targetId){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        ActiveLog log = new ActiveLog();
        log.setUserId(userId);
        log.setActivityType(activityType);
        log.setTargetId(targetId);
        log.setIp(request.getRemoteAddr());
        log.setRequestMethod(request.getMethod());
        log.setRequestUrl(request.getRequestURI());
        activeLogRepository.save(log);
    }
}

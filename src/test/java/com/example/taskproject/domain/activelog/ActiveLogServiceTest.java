package com.example.taskproject.domain.activelog;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.domain.activelog.repository.ActiveLogRepository;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActiveLogServiceTest {
    @InjectMocks
    ActiveLogService activeLogService;
    @Mock
    ActiveLogRepository activeLogRepository;

    @Test
    void test_search_log(){
        //given
        LogRequestDto request = new LogRequestDto(1L, "LOGIN", null, null, null, 0, 10, true);
        Pageable pageable = request.toPageable();

        List<ActiveLog> logs = List.of(new ActiveLog(1L, "LOGIN", 1L));
        Page<ActiveLog> page = new PageImpl<>(logs, pageable, logs.size());
        when(activeLogRepository.findActiveLogsDynamic(request, pageable)).thenReturn(page);
        //when

        Page<ActiveLog> result = activeLogService.getActiveLogsByConditions(request, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        ActiveLog log = result.getContent().get(0);
        assertThat(log.getUserId()).isEqualTo(1L);
        assertThat(log.getActivityType()).isEqualTo("LOGIN");
    }

    @Test
    void logActivity_save_test(){
        //given
        Long userId = 1L;
        String activityType = "TEST";
        Long targetId = 1L;

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.0.1");
        request.setMethod("POST");
        request.setRequestURI("/test/uri");

        //when
        activeLogService.logActivity(userId, activityType, targetId);

        //then
        ArgumentCaptor<ActiveLog> captor = ArgumentCaptor.forClass(ActiveLog.class);
        verify(activeLogRepository, times(1)).save(captor.capture());

        ActiveLog savedLog = captor.getValue();
        assertEquals(userId, savedLog.getUserId());
        assertEquals(activityType, savedLog.getActivityType());
        assertEquals(targetId, savedLog.getTargetId());
        assertEquals("192.168.0.1", savedLog.getIp());
        assertEquals("POST", savedLog.getRequestMethod());
        assertEquals("/test/uri", savedLog.getRequestUrl());
    }
}

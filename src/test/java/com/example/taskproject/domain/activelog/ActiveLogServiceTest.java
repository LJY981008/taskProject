package com.example.taskproject.domain.activelog;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.domain.activelog.repository.ActiveLogRepository;
import com.example.taskproject.domain.activelog.service.ActiveLogService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
public class ActiveLogServiceTest {
    @Autowired
    ActiveLogService activeLogService;
    @Autowired
    ActiveLogRepository activeLogRepository;
    @Autowired
    private EntityManager em;

    @Test
    void test_search_log(){
        //given
        LocalDateTime now = LocalDateTime.now();
        activeLogRepository.saveAll(List.of(
                new ActiveLog(1L, "LOGIN", 1L),
                new ActiveLog(1L, "LOGOUT", 1L),
                new ActiveLog(2L, "LOGIN", 2L),
                new ActiveLog(3L, "UPDATE", 3L)
        ));
        em.flush();
        em.clear();

        //when
        LogRequestDto request = new LogRequestDto(1L, "LOGIN", null, null, null, 0, 10, true);
        Pageable pageable = request.toPageable();
        Page<ActiveLog> result = activeLogService.getActiveLogsByConditions(request, pageable);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        ActiveLog log = result.getContent().get(0);
        assertThat(log.getUserId()).isEqualTo(1L);
        assertThat(log.getActivityType()).isEqualTo("LOGIN");
    }
}

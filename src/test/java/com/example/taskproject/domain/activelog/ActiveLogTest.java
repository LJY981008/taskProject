package com.example.taskproject.domain.activelog;

import com.example.taskproject.common.dto.LogRequestDto;
import com.example.taskproject.common.entity.ActiveLog;
import com.example.taskproject.domain.activelog.repository.ActiveLogRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ActiveLogRepositoryTest {
    @Autowired
    private ActiveLogRepository activeLogRepository;
    @Autowired
    private EntityManager em;

    @Test
    void test_search_conditions(){
        //given
        LocalDateTime now = LocalDateTime.now();
        activeLogRepository.saveAll(List.of(
                new ActiveLog(1L, "LOGIN", 1L, now.minusDays(3)),
                new ActiveLog(1L, "LOGOUT", 1L, now.minusDays(2)),
                new ActiveLog(2L, "LOGIN", 2L, now.minusDays(1)),
                new ActiveLog(3L, "UPDATE", 3L, now)
        ));
        em.flush();
        em.clear();

        //when
        LogRequestDto logRequest = new LogRequestDto(1L, "LOGIN", null, now.minusDays(4), now.minusDays(2), 0, 10, true);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ActiveLog> result = activeLogRepository.findActiveLogsDynamic(logRequest, pageable);

        //then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getActivityType()).isEqualTo("LOGIN");
        assertThat(result.getContent().get(0).getUserId()).isEqualTo(1L);
    }
}

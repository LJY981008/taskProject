package com.example.taskproject.domain.activelog.repository;

import com.example.taskproject.common.entity.ActiveLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveLogRepository extends JpaRepository<ActiveLog, Long>, ActiveLogRepositoryCustom {
}

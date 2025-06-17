package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.Task;
import com.example.taskproject.domain.statistics.repository.StatisticsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, StatisticsRepository {
    Optional<Task> findTaskByTaskIdAndDeletedFalse(Long id);
    Page<Task> findByDeletedFalse(Pageable pageable);

}

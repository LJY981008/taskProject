package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.Task;
import com.example.taskproject.domain.statistics.repository.StatisticsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, StatisticsRepository, TaskRepositoryCustom {
    Optional<Task> findTaskByTaskIdAndDeletedFalse(Long id);

    @Query("SELECT t FROM Task t JOIN FETCH t.author WHERE t.taskId = :taskId AND t.deleted = false")
    Optional<Task> findTaskByTaskId(Long taskId);
}

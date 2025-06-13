package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByTaskIdAndDeletedFalse(Long id);
}

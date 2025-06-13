package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByTaskIdAndDeletedFalse(Long id);

    @Query("SELECT t.taskStatus AS taskStatus, COUNT(t) AS count FROM Task t WHERE t.deleted = false GROUP BY t.taskStatus")
    List<StatusCount> findStatusCountJPQL();

    interface StatusCount {
        TaskStatus getTaskStatus();
        Long getCount();
    }

    Long countAllByDeletedFalse();
    Long countByDeletedFalseAndTaskStatus(TaskStatus taskStatus);
    Long countByDeletedFalseAndAuthor_UsernameAndTaskStatus(String authorUsername, TaskStatus taskStatus);

    Long countByDeletedFalseAndCreatedAtBetween(LocalDateTime createdAt, LocalDateTime createdAt2);
    Long countByDeletedFalseAndTaskStatusAndCreatedAtBetween(TaskStatus taskStatus, LocalDateTime createdAt, LocalDateTime createdAt2);
}

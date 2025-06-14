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

    @Query("""
        SELECT
            COUNT(t) AS totalTaskCount,
            COALESCE(SUM(CASE WHEN t.taskStatus = :taskStatus THEN 1 ELSE 0 END), 0) AS teamFinishTaskCount,
            COALESCE(SUM(CASE WHEN t.taskStatus = :taskStatus AND t.author.username = :authorUsername THEN 1 ELSE 0 END), 0) AS myFinishTaskCount
        FROM Task t
        WHERE t.deleted = false
        """)
    TeamTaskStatusCount countTeamTaskStatusCountJPQL(String authorUsername, TaskStatus taskStatus);

    interface TeamTaskStatusCount {
        Long getTotalTaskCount();
        Long getTeamFinishTaskCount();
        Long getMyFinishTaskCount();
    }

    Long countByDeletedFalseAndCreatedAtBetween(LocalDateTime createdAt, LocalDateTime createdAt2);
    Long countByDeletedFalseAndTaskStatusAndCreatedAtBetween(TaskStatus taskStatus, LocalDateTime createdAt, LocalDateTime createdAt2);
}

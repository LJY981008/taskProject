package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByTaskIdAndDeletedFalse(Long id);

    @Query("SELECT t.taskStatus AS taskStatus, COUNT(t) AS count FROM Task t WHERE t.deleted = false GROUP BY t.taskStatus")
    List<StatusCount> findStatusCountJPQL();

    interface StatusCount {
        TaskStatus getTaskStatus();
        Long getCount();
    }
}

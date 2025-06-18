package com.example.taskproject.domain.task.repository;

import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskRepositoryCustom {
    Page<Task> findByFilterTask(Pageable pageable, TaskStatus status, String search, Long assigneeId);
}

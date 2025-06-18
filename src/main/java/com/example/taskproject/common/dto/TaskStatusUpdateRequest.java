package com.example.taskproject.common.dto;

import com.example.taskproject.common.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskStatusUpdateRequest {
    @NotNull(message = "상태를 지정해야합니다.")
    private TaskStatus status;
}

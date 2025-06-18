package com.example.taskproject.domain.task.exception;

import com.example.taskproject.common.enums.CustomErrorCode;
import com.example.taskproject.common.exception.CustomException;

public class TaskNotFoundException extends CustomException {
    public TaskNotFoundException() {
        super(CustomErrorCode.TASK_NOT_FOUND);
    }

    public TaskNotFoundException(String customMessage) {
        super(CustomErrorCode.TASK_NOT_FOUND, customMessage);
    }
}

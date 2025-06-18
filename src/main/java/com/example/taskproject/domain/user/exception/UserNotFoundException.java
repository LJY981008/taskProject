package com.example.taskproject.domain.user.exception;

import com.example.taskproject.common.enums.CustomErrorCode;
import com.example.taskproject.common.exception.CustomException;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException() {
        super(CustomErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(String customMessage) {
        super(CustomErrorCode.USER_NOT_FOUND, customMessage);
    }
}

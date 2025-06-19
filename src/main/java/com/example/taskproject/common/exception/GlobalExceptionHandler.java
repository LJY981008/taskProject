package com.example.taskproject.common.exception;

import com.example.taskproject.common.dto.CustomErrorResponseDto;
import com.example.taskproject.common.enums.CustomErrorCode;
import com.example.taskproject.common.util.CustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponseDto> handleCustomException(CustomException e){
        CustomErrorCode errorCode = e.getErrorCode();

        CustomErrorResponseDto errorResponseDto = new CustomErrorResponseDto(errorCode.name(),e.getMessage());

        return new ResponseEntity<>(errorResponseDto, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        CustomErrorCode errorCode = CustomErrorCode.INVALID_REQUEST;
        String message = messageSource.getMessage(Objects.requireNonNull(e.getFieldError()), Locale.KOREA);
        CustomErrorResponseDto errorResponseDto = new CustomErrorResponseDto(errorCode.name(), message);

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(CustomMapper.responseToMap(errorResponseDto, false));
    }
}

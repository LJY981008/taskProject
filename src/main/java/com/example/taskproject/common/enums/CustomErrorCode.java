package com.example.taskproject.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 잘못되었습니다."), // (회원가입, 로그인) 이메일 형식 틀림
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호 형식이 잘못되었습니다."), // (회원가입, 로그인) 비번 형식 틀림
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유저네임 형식이 잘못되었습니다."), // (회원가입, 로그인) 유저네임 형식 틀림
    ROLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 UserRole"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이상해씨"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_NOT_ENTERED(HttpStatus.BAD_REQUEST, "댓글이 작성되지 않았습니다."),
    COMMENT_IS_EQUAL(HttpStatus.BAD_REQUEST, "수정된 댓글이 기존 댓글과 동일합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
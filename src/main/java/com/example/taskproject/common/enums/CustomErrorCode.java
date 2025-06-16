package com.example.taskproject.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 잘못되었습니다."), // (회원가입, 로그인) 이메일 형식 틀림
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호 형식이 잘못되었습니다."), // (회원가입, 로그인) 비번 형식 틀림
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유저네임 형식이 잘못되었습니다."), // (회원가입, 로그인) 유저네임 형식 틀림
    ROLE_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 UserRole"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이상해씨"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED,"잘못된 사용자명 또는 비밀번호입니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않거나 탈퇴한 유저입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    ;
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED,"잘못된 사용자명 또는 비밀번호입니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않거나 탈퇴한 유저입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    COMMENT_NOT_ENTERED(HttpStatus.BAD_REQUEST, "댓글이 작성되지 않았습니다."),
    COMMENT_IS_EQUAL(HttpStatus.BAD_REQUEST, "수정된 댓글이 기존 댓글과 동일합니다."),;

    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
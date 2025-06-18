package com.example.taskproject.common.enums;

import lombok.Getter;

@Getter
public enum ActivityType {
    TASK_CREATED("작업 생성"),
    TASK_UPDATED("작업 수정"),
    TASK_DELETED("작업 삭제"),
    COMMENT_CREATED("댓글 생성"),
    COMMENT_UPDATED("댓글 수정"),
    COMMENT_DELETED("댓글 삭제"),
    USER_REGISTERED("회원가입"),
    USER_LOGGED_IN("로그인"),
    USER_DELETED("회원탈퇴");

    private final String typeDescription;

    ActivityType(String typeDescription){
        this.typeDescription = typeDescription;
    }
}

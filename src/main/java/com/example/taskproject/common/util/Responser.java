package com.example.taskproject.common.util;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class Responser {
    public static <T>ResponseEntity<T> responseEntity(T body, HttpStatusCode status) {
        return ResponseEntity.status(status).body(body);
    }
}

package com.example.taskproject.common.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PagedResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int size;
    private final int number;

    public PagedResponse(List<T> content, long totalElements, int totalPages, int size, int number){
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.number = number;
    }

    public PagedResponse(org.springframework.data.domain.Page<T> page){
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.size = page.getSize();
        this.number = page.getNumber();
    }
}

package com.example.taskproject.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "active_logs")
public class ActiveLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String ip;

    private String requestMethod;

    private String requestUrl;

    private String activityType;

    private Long targetId;
}

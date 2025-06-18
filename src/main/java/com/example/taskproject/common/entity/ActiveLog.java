package com.example.taskproject.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "active_logs")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class ActiveLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String ip;

    private String requestMethod;

    private String requestUrl;

    private String activityType;

    private Long targetId;

    @CreatedDate
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    public ActiveLog(Long userId, String activityType, Long targetId){
        this.userId = userId;
        this.activityType = activityType;
        this.targetId = targetId;
    }
}

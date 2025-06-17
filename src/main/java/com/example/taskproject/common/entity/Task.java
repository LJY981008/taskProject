package com.example.taskproject.common.entity;

import com.example.taskproject.common.enums.TaskPriority;
import com.example.taskproject.common.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Tasks")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String title;

    @Column(name = "description")
    private String contents;

    @Column(name = "priority")
    private TaskPriority taskPriority;

    @Column(name = "status")
    private TaskStatus taskStatus;

    @Column(name = "dueDate")
    private LocalDateTime deadline;
    //추가
    private LocalDateTime startedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    public Task(){
    }

    public Task(Long taskId, String title, User author){
        this.taskId = taskId;
        this.title = title;
        this.author = author;
    }
}

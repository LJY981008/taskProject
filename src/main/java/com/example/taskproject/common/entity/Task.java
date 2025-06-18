package com.example.taskproject.common.entity;

import com.example.taskproject.common.enums.TaskPriority;
import com.example.taskproject.common.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "Tasks")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long taskId;

    private String title;

    @Column(name = "description")
    private String contents;

    @Column(name = "priority")
    @Enumerated(value = EnumType.STRING)
    private TaskPriority taskPriority;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @Column(name = "dueDate")
    private LocalDateTime dueDate = LocalDateTime.now();
    //추가
    private LocalDateTime startedAt = LocalDateTime.now();

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

    public void update(String title, String contents, TaskPriority taskPriority,LocalDateTime dueDate, User manager){
        Optional.ofNullable(title).ifPresent(t -> this.title = t);
        Optional.ofNullable(contents).ifPresent(c -> this.contents = c);
        Optional.ofNullable(taskPriority).ifPresent(tp -> this.taskPriority = tp);
        Optional.ofNullable(dueDate).ifPresent(d -> this.dueDate = d);
        Optional.ofNullable(manager).ifPresent(m -> this.manager = m);
    }
}

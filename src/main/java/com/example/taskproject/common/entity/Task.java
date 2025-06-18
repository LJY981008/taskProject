package com.example.taskproject.common.entity;

import com.example.taskproject.common.enums.TaskPriority;
import com.example.taskproject.common.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private TaskPriority taskPriority;

    @Column(name = "status")
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

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Task(){
    }

    public Task(Long taskId, String title, User author){
        this.taskId = taskId;
        this.title = title;
        this.author = author;
    }

    @Override
    public void delete() {
        super.delete();

        if(!this.comments.isEmpty()) {
            this.comments.forEach(Comment::delete);
        }
    }
}

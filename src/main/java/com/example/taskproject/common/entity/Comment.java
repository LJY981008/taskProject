package com.example.taskproject.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;


    public Comment() {
    }

    public Comment(String contents, User author, Task task){
        this.contents = contents;
        this.author = author;
        this.task = task;
    }

    public void update(String newContents) {
        this.contents = newContents;
    }
}

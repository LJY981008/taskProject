package com.example.taskproject.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    private String username;

    @OneToMany(mappedBy = "author")
    private List<Task> writeTasks;

    @OneToMany(mappedBy = "manager")
    private List<Task> managedTasks;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
}

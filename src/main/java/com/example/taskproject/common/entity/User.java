package com.example.taskproject.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

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


    public User(Long userId, String email, String userName){
        this.userId = userId;
        this.email = email;
        this.username = userName;
    }
}

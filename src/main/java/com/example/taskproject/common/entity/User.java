package com.example.taskproject.common.entity;

import com.example.taskproject.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    public User(String email, String password, String username, String name) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.name = name;
        this.role = UserRole.USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    private String email;

    private String password;

    private String username;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

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

package com.example.taskproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskProjectApplication.class, args);
    }

}

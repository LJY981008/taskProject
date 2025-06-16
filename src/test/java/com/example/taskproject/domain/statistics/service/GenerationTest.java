package com.example.taskproject.domain.statistics.service;

import com.example.taskproject.common.entity.Task;
import com.example.taskproject.common.entity.User;
import com.example.taskproject.common.enums.TaskStatus;
import com.example.taskproject.domain.task.repository.TaskRepository;
import com.example.taskproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

@SpringBootTest
public class GenerationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void generateTaskDataTest() {

        User my = mock(User.class);
        User other = mock(User.class);
        ReflectionTestUtils.setField(my, "email", "kimyongjun@naver.com");
        ReflectionTestUtils.setField(my, "username", "김용준");
        ReflectionTestUtils.setField(other, "email", "other@example.com");
        ReflectionTestUtils.setField(other, "username", "Other");
        userRepository.save(my);
        userRepository.save(other);


        List<Task> list = new ArrayList<>();

        for (int i = 0; i < 33; i++) {
            Task task = mock(Task.class);
            ReflectionTestUtils.setField(task, "taskStatus", TaskStatus.TODO);
            ReflectionTestUtils.setField(task, "author", my);
            ReflectionTestUtils.setField(task, "deadline", LocalDateTime.of(2025, 6, 15, 0,0,0));
            list.add(task);
        }

        for (int i = 0; i < 33; i++) {
            Task task = mock(Task.class);
            ReflectionTestUtils.setField(task, "taskStatus", TaskStatus.IN_PROGRESS);
            ReflectionTestUtils.setField(task, "author", other);
            ReflectionTestUtils.setField(task, "deadline", LocalDateTime.of(2025, 6, 15, 0,0,0));
            list.add(task);
        }

        for (int i = 0; i < 33; i++) {
            Task task = mock(Task.class);
            ReflectionTestUtils.setField(task, "taskStatus", TaskStatus.DONE);
            ReflectionTestUtils.setField(task, "author", other);
            ReflectionTestUtils.setField(task, "deadline", LocalDateTime.of(2025, 6, 15, 0,0,0));
            list.add(task);
        }

        taskRepository.saveAll(list);
    }
}

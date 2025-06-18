package com.example.taskproject.domain.user.repository;

import com.example.taskproject.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndDeletedFalse(String email);

    Optional<User> findUserByUsernameAndDeletedFalse(String username);

    Optional<User> findByUserIdAndDeletedFalse(Long userId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findAllByDeletedFalse();
}

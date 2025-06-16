package com.example.taskproject.domain.user.repository;

import com.example.taskproject.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndDeletedFalse(String email);

    Optional<User> findUserByUsernameAndDeletedFalse(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.deleted = true, u.deletedAt = CURRENT TIMESTAMP WHERE u.userId = :id")
    void softDeleteById(@Param("id") Long id);
}

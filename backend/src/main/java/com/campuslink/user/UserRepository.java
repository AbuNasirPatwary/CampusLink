package com.campuslink.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByStudentId(String studentId);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByStudentId(String studentId);
    List<User> findAllByOrderByCreatedAtDesc();
    List<User> findByRoleAndStatusOrderByCreatedAtDesc(Role role, AccountStatus status);
}
